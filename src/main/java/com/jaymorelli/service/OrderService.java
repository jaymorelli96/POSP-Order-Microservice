package com.jaymorelli.service;

import java.time.LocalDateTime;
import java.util.Objects;

import com.jaymorelli.dto.OrderDTO;
import com.jaymorelli.model.Order;
import com.jaymorelli.repository.OrderRepository;
import com.jaymorelli.validator.OrderValidator;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.server.ServerWebInputException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    
    
    @Autowired
    OrderRepository orderRepository;

    private final Validator validator = new OrderValidator();

    /**
    * Create a new order document.
    * It validates de DTO and then map to Order object.
    * Finally will call MongoDB repository to create a new order. 
    * @param dto Order DTO
    * @return Mono of the newly created object.
    */
    public Mono<Order> createOrder(Mono<OrderDTO> dto) {

        return dto.flatMap(orderDto -> {
            this.validate(orderDto);
            Order order = mapperOrderDTOToEntity(orderDto);
            return orderRepository.save(order);
        });
    }

    /**
    * Update a given order.
    * It validates de DTO and then map to Order object.
    * Finally will call MongoDB repository to update the order. 
    * @param dto Order DTO
    * @return Mono of the updated object.
    */
    public Mono<Order> updateOrder(String id, Mono<OrderDTO> dto) {
       return dto.flatMap(orderDto -> {
            this.validate(orderDto);
            Order order = mapperOrderDTOToEntity(orderDto);
            return orderRepository.findById(id).flatMap(data -> {
                data.setItems(order.getItems());
                data.setTable(order.getTable());
                data.setCreatedAt(order.getCreatedAt());
                data.setTotalCost(order.getTotalCost());
                return orderRepository.save(data);
            });
        });
    }

    /**
    * Get all orders sorted by the type and direction.
    * @param type Sort by type
    * @param sort Sort direction
    * @return Flux of orders.
    */
    public Flux<Order> getOrders(String sort, String type) {
        return orderRepository.findAll(Sort.by(Sort.Direction.fromString(sort), type));
    }

    /**
    * Get all orders.
    * @return Flux of orders.
    */
    public Flux<Order> getOrders() {
        return orderRepository.findAll();
    }
      
    /**
    * Get an order by id.
    * @param id order id
    * @return Mono of order.
    */
    public Mono<Order> getOrder(String id) {
        return orderRepository.findById(id);
    }

    /**
    * Remove an order by id.
    * @param id order id
    * @return The Order object that has been removed
    */
    public Mono<Order> removeOrder(String id) {
        return this.getOrder(id)
                        .flatMap(
                            orderToBeDeleted -> this.orderRepository
                                                        .delete(orderToBeDeleted)
                                                        .then(Mono.just(orderToBeDeleted))
                            )
                        .log();
    }


    /**
     * Validade a orderDTO. It will call the OrderValidator class for validation.
     * It will throw ServerWebInputException if orderDTO.table or orderDTO.item is empty/null.
     * @param orderDTO Order DTO
     * @throws ServerWebInputException
     */
    private void validate(OrderDTO orderDTO) {
        
		Errors errors = new BeanPropertyBindingResult(orderDTO, "orderDTO");
		validator.validate(orderDTO, errors);
		if (errors.hasErrors()) {
			throw new ServerWebInputException(errors.getAllErrors().get(0).toString()); // (3)
		}
	}


    /**
     * Mapper for OrderDTO to Entity.
     * It will call calculate total cost of the order object and also set the createdAt property.
     * @param dto
     * @return Order
     */
    public Order mapperOrderDTOToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(dto.getItems());
        order.setTable(dto.getTable());
        order.setTotalCost(order.calculateTotalCost());

        return order;
    }

}

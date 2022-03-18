package com.jaymorelli.service;

import java.time.LocalDateTime;

import com.jaymorelli.dto.OrderDTO;
import com.jaymorelli.model.Order;
import com.jaymorelli.repository.OrderRepository;
import com.jaymorelli.validator.OrderValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import reactor.core.publisher.Mono;

@Service
public class OrderService {
    
    
    @Autowired
    OrderRepository orderRepository;

    private final Validator validator = new OrderValidator();

    public Mono<Order> createOrder(Mono<OrderDTO> dto) {

        Mono<Order> result = dto.flatMap(orderDto -> {
            this.validate(orderDto);
            Order order = mapperOrderDTOToEntity(orderDto);
            return orderRepository.save(order);
        });    

        return result;
    }

    private void validate(OrderDTO orderDTO) {
        
		Errors errors = new BeanPropertyBindingResult(orderDTO, "orderDTO");
		validator.validate(orderDTO, errors);
		if (errors.hasErrors()) {
			throw new ServerWebInputException(errors.getAllErrors().get(0).toString()); // (3)
		}
	}


    /**
     * Mapper for OrderDTO to Entity.
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

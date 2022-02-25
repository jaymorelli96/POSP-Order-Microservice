package com.jaymorelli.handler;

import java.time.LocalDateTime;

import com.jaymorelli.dto.OrderDTO;
import com.jaymorelli.model.Order;
import com.jaymorelli.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Service
public class OrderHandler {

    @Autowired
    OrderRepository orderRepository;


    /**
     * Recieve POST request from the router to save an Order object into the MongoDB
     * @param request ServerRequest
     * @return ServerResponse with the Order object in the body 
     */
    public Mono<ServerResponse> createOrder(ServerRequest request) {
        Mono<OrderDTO> dto = request.bodyToMono(OrderDTO.class);
        
        Mono<Order> result = dto.flatMap(orderDto -> {

            Order order = mapperOrderDTOToEntity(orderDto);
            return orderRepository.save(order);
        });        


        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)    
                .body(result, Order.class);
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

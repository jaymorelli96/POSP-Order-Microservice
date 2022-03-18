package com.jaymorelli.handler;

import java.time.LocalDateTime;

import com.jaymorelli.dto.OrderDTO;
import com.jaymorelli.model.Order;
import com.jaymorelli.repository.OrderRepository;
import com.jaymorelli.service.OrderService;
import com.jaymorelli.validator.OrderValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;

import reactor.core.publisher.Mono;

@Service
public class OrderHandler {


    private final OrderService service;

    public OrderHandler(OrderService service) {
        this.service = service;
    }


    /**
     * Recieve POST request from the router to save an Order object into the MongoDB
     * @param request ServerRequest
     * @return ServerResponse with the Order object in the body 
     */
    public Mono<ServerResponse> createOrder(ServerRequest request) {
        //1. Extract JSON object from Server Request.
        Mono<OrderDTO> dto = request.bodyToMono(OrderDTO.class);
        
        //2. Send request to service and get the Order object.
        Mono<Order> result = service.createOrder(dto);   

        //3. Return server response and the order object in the body.
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)    
                .body(result, Order.class);
    }

}

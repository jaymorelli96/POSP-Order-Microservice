package com.jaymorelli.controller;

import com.jaymorelli.service.OrderService;
import com.jaymorelli.model.Order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController()
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping()
    public Mono<Order> createOrder(@RequestBody Order obj) {
        return orderService.createOrder(obj).log();
    }
    
}

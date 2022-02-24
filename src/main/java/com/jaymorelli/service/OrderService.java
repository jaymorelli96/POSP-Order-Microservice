package com.jaymorelli.service;

import com.jaymorelli.model.Order;
import com.jaymorelli.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public Mono<Order> createOrder(Order obj) {
        return orderRepository.save(obj);
    }
    
}

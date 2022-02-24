package com.jaymorelli.repository;


import com.jaymorelli.model.Order;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;


public interface OrderRepository extends ReactiveMongoRepository<Order, String>{
    
}

package com.jaymorelli.repository;


import com.jaymorelli.model.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface OrderRepository extends ReactiveMongoRepository<Order, String>{
    
}

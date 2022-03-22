package com.jaymorelli.repository;


import com.jaymorelli.model.Order;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;


public interface OrderRepository extends ReactiveMongoRepository<Order, String>{
    
    @Query(value = "{'_id': ?0}", delete = true)
    Mono<Void> removeByObjectId(ObjectId id);

        
    @Query("{'_id': ?0}")
    Mono<Order> findByObjectId(ObjectId id);

}


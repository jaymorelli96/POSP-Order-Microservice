package com.jaymorelli.handler;

import java.util.Optional;

import com.jaymorelli.dto.OrderDTO;
import com.jaymorelli.model.Order;
import com.jaymorelli.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
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
        
        //2. Send request to service.
        Mono<Order> result = service.createOrder(dto);   

        //3. Return server response and the order object in the body.
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)    
                .body(result, Order.class);
    }

    
    /**
     * Recieve PUT request from the router to update an Order object in the MongoDB
     * @param request ServerRequest
     * @return ServerResponse with the Order object in the body 
     */
    public Mono<ServerResponse> updateOrder(ServerRequest request) {
        //1. Extract JSON object and id from Server Request.
        Mono<OrderDTO> dto = request.bodyToMono(OrderDTO.class);
        Optional<String> id = request.queryParam("id");
        
        //2. Send request to service.
        Mono<Order> result;
        if(id.isPresent()) result = service.updateOrder(id.get(), dto);
        else {
            return ServerResponse.badRequest()
            .contentType(MediaType.APPLICATION_JSON)    
            .body("ID is not present", String.class);
        }

        //3. Return server response and the order object in the body.
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)    
                .body(result, Order.class);
    }

    /**
     * GET Request to retrieve Order(s). 
     * Server request may contain query params, where it can return a specific order or sort all orders.
     * @param request ServerRequest
     * @return ServerResponse Order object(s) in the body 
     */
    public Mono<ServerResponse> getOrder(ServerRequest request) {
        Optional<String> id = request.queryParam("id");
        Optional<String> sort = request.queryParam("sort");
        Optional<String> type = request.queryParam("type");
            

        //1. Check if id query param is present.
        if(id.isPresent()) 
        //2. Send request to service.
        {
            Mono<Order> result = service.getOrder(id.get());
            
            //3. Return server response and the order object in the body.
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)    
                .body(result, Order.class);
        } 

        //4. Check if sort and type query param is present.
        else if(sort.isPresent() && type.isPresent())
        {
            Flux<Order> result = service.getOrders(sort.get(), type.get());  
            
            //5. Return server response and the order object array in the body.
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)    
                .body(result, Order.class);
        } 
        //5. Default - Get all orders
        else {
            Flux<Order> result = service.getOrders();  
            
            //6. Return server response and the order object array in the body.
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)    
                .body(result, Order.class);
        }

    }

    /**
     * Recieve DELETE request from the router to remove an Order object from the MongoDB
     * @param request ServerRequest
     * @return ServerResponse 
     */
    public Mono<ServerResponse> removeOrder(ServerRequest request) {
        //1. Extract id from Server Request.
        Optional<String> id = request.queryParam("id");
        
        //2. Send request to service.
        if(id.isPresent()) service.removeOrder(id.get());

        //3. Return server response and the order object in the body.
        return ServerResponse.ok().build();
    }

}

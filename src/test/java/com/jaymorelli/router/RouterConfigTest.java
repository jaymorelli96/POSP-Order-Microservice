package com.jaymorelli.router;

import static org.mockito.Mockito.when;

import com.jaymorelli.handler.OrderHandler;
import com.jaymorelli.model.Order;
import com.jaymorelli.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class RouterConfigTest {

    private OrderService orderService;
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        OrderHandler orderHandler = new OrderHandler(orderService);
        RouterFunction<?> routes = new RouterConfig()
                .routerFunction(orderHandler);     
        client = WebTestClient.bindToRouterFunction(routes)
                .build();
    }

    
    @Test
    void testRouterFunction_CreateOrder_POST() {
        //1. Prepare data
        Order order = new Order();
        order.setTable("Table Test");
        Mono<Order> result = Mono.just(order);

        //2. Mock service
        when(orderService.createOrder(any()))
        .thenReturn(result);

        //3. Mock call and assert
        client.post()
                .uri(uriBuilder -> uriBuilder.path("/order")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(result), Order.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.table").isNotEmpty()
        ;
    }

    @Test
    void testRouterFunction_GetOrder_GET() {
        //1. Prepare data
        Order order = new Order();
        order.setTable("Table Test");
        Flux<Order> result = Flux.just(order, order);


        //2. Mock service
        when(orderService.getOrders())
        .thenReturn(result);

        //3. Mock call and assert
        client.get()
                .uri(uriBuilder -> uriBuilder.path("/order")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
        ;
    }

    @Test
    void testRouterFunction_GetOrderById_GET() {
        //1. Prepare data
        Order order = new Order();
        order.setId("id");
        order.setTable("Table Test");
        Mono<Order> result = Mono.just(order);


        //2. Mock service
        when(orderService.getOrder("id"))
        .thenReturn(result);

        //3. Mock call and assert
        client.get()
                .uri(uriBuilder -> uriBuilder.path("/order")
                        .queryParam("id", "id")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
        ;
    }

    @Test
    void testRouterFunction_GetOrdersSorted_GET() {
        //1. Prepare data
        Order order = new Order();
        order.setId("id");
        order.setTable("Table Test");
        Flux<Order> result = Flux.just(order, order);


        //2. Mock service
        when(orderService.getOrders("asc", "table"))
        .thenReturn(result);

        //3. Mock call and assert
        client.get()
                .uri(uriBuilder -> uriBuilder.path("/order")
                        .queryParam("sort", "asc")
                        .queryParam("type", "table")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
        ;
    }
}

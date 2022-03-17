package com.jaymorelli.router;

import static org.mockito.Mockito.when;

import com.jaymorelli.handler.OrderHandler;
import com.jaymorelli.model.Order;
import com.jaymorelli.repository.OrderRepository;
import com.jaymorelli.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;

import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void testRouterFunction() {
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
}

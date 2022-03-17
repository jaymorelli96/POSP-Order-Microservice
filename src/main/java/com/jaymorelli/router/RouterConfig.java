package com.jaymorelli.router;

import com.jaymorelli.handler.OrderHandler;
import com.jaymorelli.model.Order;
import com.mongodb.internal.connection.Server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    /**
     * Router function for the endpoints of the Order service.
     * @return A server response.
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction(OrderHandler handler) {
        return RouterFunctions
            .route(RequestPredicates.POST("/order"), handler::createOrder);
    }

}



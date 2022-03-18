package com.jaymorelli.router;

import com.jaymorelli.handler.OrderHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    private static final String ENDPOINT_PATH = "/order";

    /**
     * Router function for the endpoints of the Order service.
     * @return A server response.
     */
    @Bean
    public RouterFunction<ServerResponse> routerFunction(OrderHandler handler) {
        return RouterFunctions
            .route(RequestPredicates.POST(ENDPOINT_PATH), handler::createOrder)
            .andRoute(RequestPredicates.GET(ENDPOINT_PATH), handler::getOrder)
            .andRoute(RequestPredicates.DELETE(ENDPOINT_PATH), handler::removeOrder);
    }

}



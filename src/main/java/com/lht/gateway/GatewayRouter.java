package com.lht.gateway;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.RouteMatcher;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Leo
 * @date 2024/06/02
 */
@Component
public class GatewayRouter {


    @Autowired
    HelloHandler helloHandler;

    @Autowired
    GatewayHandler gatewayHandler;

    @Bean
    public RouterFunction<?> helloRouterFunction(){
//        return route(GET("/hello"), request -> ServerResponse.ok().body(Mono.just("hello,gateway"), String.class));
//        return route(GET("/hello"), request -> ServerResponse.ok().bodyValue("hello,gateway"));
//        return route(GET("/hello"), request -> helloHandler.handle(request));
        return route(GET("/hello"), helloHandler::handle);
    }

    @Bean
    public RouterFunction<?> gwRouterFunction(){
        return route(GET("/gw").or(POST("/gw/**")), gatewayHandler::handle);
    }



}

package com.lht.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Leo
 * @date 2024/06/10
 */
public interface GatewayPlugin {

    String GATEWAY_PREFIX = "/gw";


    void start();

    void stop();

    String getName();
    boolean support(ServerWebExchange exchange);

    Mono<Void> handler(ServerWebExchange exchange);


}

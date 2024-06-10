package com.lht.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Leo
 * @date 2024/06/10
 */
public interface GatewayPluginChain {

    Mono<Void> handler(ServerWebExchange exchange);


}

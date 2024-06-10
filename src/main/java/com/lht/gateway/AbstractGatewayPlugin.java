package com.lht.gateway;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Leo
 * @date 2024/06/10
 */
public abstract class AbstractGatewayPlugin implements GatewayPlugin{

    protected String prefix = GATEWAY_PREFIX + "/" + getName() + "/";


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


    @Override
    public Mono<Void> handler(ServerWebExchange exchange) {
        boolean supported = support(exchange);
        System.out.println(" =========> plugin[" + getName() + "], support=" + supported);
        exchange.getResponse().getHeaders().add("lht.gw.plugin", getName());

        return supported ? doHandler(exchange) : Mono.empty();
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    public abstract Mono<Void> doHandler(ServerWebExchange exchange);
    public abstract boolean doSupport(ServerWebExchange exchange);
}

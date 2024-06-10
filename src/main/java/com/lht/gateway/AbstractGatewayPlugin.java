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
    public Mono<Void> handler(ServerWebExchange exchange, GatewayPluginChain chain) {
        boolean supported = support(exchange);
        System.out.println(" =========> plugin[" + getName() + "], support=" + supported);


        return supported ? doHandler(exchange,chain) : chain.handler(exchange);
    }

    @Override
    public boolean support(ServerWebExchange exchange) {
        return doSupport(exchange);
    }

    public abstract Mono<Void> doHandler(ServerWebExchange exchange, GatewayPluginChain gatewayPluginChain);
    public abstract boolean doSupport(ServerWebExchange exchange);
}

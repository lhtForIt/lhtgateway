package com.lht.gateway;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Leo
 * @date 2024/06/10
 */
public class DefaultGatewayPluginChain implements GatewayPluginChain {

    List<GatewayPlugin> plugins;
    int index = 0;

    public DefaultGatewayPluginChain(List<GatewayPlugin> plugins) {
        this.plugins = plugins;
    }

    @Override
    public Mono<Void> handler(ServerWebExchange exchange) {
        //惰性
        return Mono.defer(()->{
            if (index < plugins.size()) {
                return plugins.get(index++).handler(exchange, this);
            }
            return Mono.empty();
        });
    }
}

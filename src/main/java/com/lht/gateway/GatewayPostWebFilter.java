package com.lht.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author Leo
 * @date 2024/06/07
 */
@Slf4j
@Component
public class GatewayPostWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).doFinally(t->{
            log.debug("===>> post filter");
            exchange.getAttributes().forEach((k, v) -> System.out.println(k + ":" + v));
        });
    }
}

package com.lht.gateway.filter;

import com.lht.gateway.GatewayFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Leo
 * @date 2024/06/10
 */
@Slf4j
@Component("headerFilter")
public class HeaderFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        log.info("========>>>> filters: header filter....");
        exchange.getRequest().getHeaders().forEach((k, v) -> System.out.println(k + ":" + v));
        return Mono.empty();
    }
}

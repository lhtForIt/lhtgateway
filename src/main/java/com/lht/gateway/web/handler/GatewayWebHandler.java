package com.lht.gateway.web.handler;

import com.lht.gateway.GatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Leo
 * @date 2024/06/07
 */
@Slf4j
@Component
public class GatewayWebHandler implements WebHandler {

    @Autowired
    List<GatewayPlugin> plugins;



    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {

        log.info(" =========> lht gateway web handler......");

        if (CollectionUtils.isEmpty(plugins)) {
            String mock= """
                    {"result":"no plugin"}
                    """;
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
        }

        for (GatewayPlugin plugin : plugins) {
            if (plugin.support(exchange)) {
                return plugin.handler(exchange);
            }
        }

        String mock= """
                {"result":"no supported plugin"}
                """;

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));

    }
}

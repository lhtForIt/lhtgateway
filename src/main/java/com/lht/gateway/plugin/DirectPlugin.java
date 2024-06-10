package com.lht.gateway.plugin;

import com.lht.gateway.AbstractGatewayPlugin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Leo
 * @date 2024/06/10
 */
@Slf4j
@Component("direct")
public class DirectPlugin extends AbstractGatewayPlugin {

    public static final String NAME = "direct";

    @Override
    public Mono<Void> doHandler(ServerWebExchange exchange) {

        log.info("=======>>>>>>> [DirectPlugin] ....");
        String backend = exchange.getRequest().getQueryParams().getFirst("backend");
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("lht.gw.version", "v1.0.0");

        if (StringUtils.isEmpty(backend)) {
            return requestBody.flatMap(t -> exchange.getResponse().writeWith(Mono.just(t))).then();
        }

        return LhtRpcPlugin.getResponseFromRequest(exchange, backend, requestBody);

    }

    @Override
    public boolean doSupport(ServerWebExchange exchange) {
        return exchange.getRequest().getPath().value().startsWith(prefix);
    }

    @Override
    public String getName() {
        return NAME;
    }
}

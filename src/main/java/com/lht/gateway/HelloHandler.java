package com.lht.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author Leo
 * @date 2024/06/02
 */
@Component
public class HelloHandler {

    Mono<ServerResponse> handle(ServerRequest request) {

        String url="http://localhost:8081/lhtrpc";
        String requestJson = """
                {
                  "service": "com.lht.lhtrpc.demo.api.UserService",
                  "methodSign": "find@1_int",
                  "args": [200]
                }
                """;
        WebClient webClient = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = webClient.post()
                .header("Content-Type", "application/json")
                .bodyValue(requestJson).retrieve().toEntity(String.class);
        Mono<String> body = entity.map(ResponseEntity::getBody);
        body.subscribe(source -> System.out.println("response:" + source));
        return ServerResponse.ok()
                .header("Content-Type", "application/json")
                .header("lht.gw.version", "v1.0.0")
                .body(body, String.class);
    }
}

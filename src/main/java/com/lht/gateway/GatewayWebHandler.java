package com.lht.gateway;

import com.lht.lhtrpc.core.api.LoadBalancer;
import com.lht.lhtrpc.core.api.RegistryCenter;
import com.lht.lhtrpc.core.cluster.RandomRibonLoadBalancer;
import com.lht.lhtrpc.core.meta.InstanceMeta;
import com.lht.lhtrpc.core.meta.ServiceMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
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
    RegistryCenter rc;
    LoadBalancer<InstanceMeta> loadBalancer = new RandomRibonLoadBalancer();


    @Override
    public Mono<Void> handle(ServerWebExchange exchange) {

        log.debug("====> Lht Gateway Web Handler ....");

        //1.通过请求路径获取服务名
        String service = exchange.getRequest().getPath().value().substring(4);
        ServiceMeta serviceMeta = ServiceMeta.builder().app("lht-app").namespace("public").env("dev").name(service).build();
        //2.通过rc拿到服务实例列表
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        //3.通过负载均衡算法选择一个实例，并获取其url
        InstanceMeta meta = loadBalancer.choose(instanceMetas);
        String url = meta.toUrl();


        //4.拿到请求的body
        Flux<DataBuffer> requestBody = exchange.getRequest().getBody();
        //5.通过webclient发送post请求
        WebClient webClient = WebClient.create(url);
        Mono<ResponseEntity<String>> entity = webClient.post()
                .header("Content-Type", "application/json")
                .body(requestBody,DataBuffer.class).retrieve().toEntity(String.class);
        //6.通过entity拿到响应报文
        Mono<String> body = entity.map(ResponseEntity::getBody);
//        body.subscribe(source -> System.out.println("response:" + source));

        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("lht.gw.version", "v1.0.0");
        //7.组装响应报文
        return body.flatMap(d -> exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(d.getBytes()))));



//        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap("hello world,lht".getBytes())));

    }
}

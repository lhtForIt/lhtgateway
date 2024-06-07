package com.lht.gateway;

import com.lht.lhtrpc.core.api.LoadBalancer;
import com.lht.lhtrpc.core.api.RegistryCenter;
import com.lht.lhtrpc.core.cluster.RandomRibonLoadBalancer;
import com.lht.lhtrpc.core.meta.InstanceMeta;
import com.lht.lhtrpc.core.meta.ServiceMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Leo
 * @date 2024/06/02
 */
@Component
public class GatewayHandler {

    @Autowired
    RegistryCenter rc;
    LoadBalancer<InstanceMeta> loadBalancer = new RandomRibonLoadBalancer();

    Mono<ServerResponse> handle(ServerRequest request) {

        //1.通过请求路径获取服务名
        String service = request.path().substring(4);
        ServiceMeta serviceMeta = ServiceMeta.builder().app("lht-app").namespace("public").env("dev").name(service).build();
        //2.通过rc拿到服务实例列表
        List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);
        //3.通过负载均衡算法选择一个实例，并获取其url
        InstanceMeta meta = loadBalancer.choose(instanceMetas);
        String url= meta.toUrl();


        //4.拿到请求的body
        Mono<String> requestJson = request.bodyToMono(String.class);
        return requestJson.flatMap(t->{
            //5.通过webclient发送post请求
            WebClient webClient = WebClient.create(url);
            Mono<ResponseEntity<String>> entity = webClient.post()
                    .header("Content-Type", "application/json")
                    .bodyValue(t).retrieve().toEntity(String.class);
            //6.通过entity拿到响应报文
            Mono<String> body = entity.map(ResponseEntity::getBody);
            body.subscribe(source -> System.out.println("response:" + source));
            //7.组装响应报文
            return ServerResponse.ok()
                    .header("Content-Type", "application/json")
                    .header("lht.gw.version", "v1.0.0")
                    .body(body, String.class);
        });

    }
}

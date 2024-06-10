package com.lht.gateway;

import com.lht.lhtrpc.core.api.RegistryCenter;
import com.lht.lhtrpc.core.registry.lht.LhtRegistryCenter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.Property;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * @author Leo
 * @date 2024/06/07
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RegistryCenter rc() {
        return new LhtRegistryCenter();
    }


    @Bean
    ApplicationRunner applicationRunner(ApplicationContext applicationContext){
        return args -> {
            SimpleUrlHandlerMapping handlerMapping = applicationContext.getBean(SimpleUrlHandlerMapping.class);
            Properties mapping = new Properties();
            mapping.put("/ga/**", "gatewayWebHandler");
            handlerMapping.setMappings(mapping);
            handlerMapping.initApplicationContext();
            System.out.println("=========> lht gateway start.........");
        };

    }


}

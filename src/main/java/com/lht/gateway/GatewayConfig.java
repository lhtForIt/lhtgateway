package com.lht.gateway;

import com.lht.lhtrpc.core.api.RegistryCenter;
import com.lht.lhtrpc.core.registry.lht.LhtRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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





}

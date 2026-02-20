package com.microservices.tnb.orderservice.infrastructure.client.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public RequestInterceptor feignJwtRequestInterceptor() {
        return new FeignJwtRequestInterceptor();
    }
}

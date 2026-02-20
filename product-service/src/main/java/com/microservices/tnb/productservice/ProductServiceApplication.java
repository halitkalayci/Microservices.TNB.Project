package com.microservices.tnb.productservice;

import com.microservices.tnb.productservice.application.service.ProductCommandService;
import com.microservices.tnb.productservice.application.service.ProductQueryServiceImpl;
import com.microservices.tnb.productservice.domain.port.ProductCommandPort;
import com.microservices.tnb.productservice.domain.port.ProductQueryPort;
import com.microservices.tnb.productservice.service.ProductQueryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public ProductCommandService productCommandService(ProductCommandPort productCommandPort) {
        return new ProductCommandService(productCommandPort);
    }

    @Bean
    public ProductQueryService productQueryService(ProductQueryPort productQueryPort) {
        return new ProductQueryServiceImpl(productQueryPort);
    }

}

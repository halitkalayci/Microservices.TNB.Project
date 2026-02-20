package com.microservices.tnb.orderservice;

import com.microservices.tnb.orderservice.application.service.OrderCommandService;
import com.microservices.tnb.orderservice.application.service.OrderQueryServiceImpl;
import com.microservices.tnb.orderservice.domain.port.OrderCommandPort;
import com.microservices.tnb.orderservice.domain.port.OrderQueryPort;
import com.microservices.tnb.orderservice.infrastructure.client.ProductServiceClient;
import com.microservices.tnb.orderservice.service.OrderQueryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    public OrderCommandService orderCommandService(OrderCommandPort orderCommandPort,
                                                   ProductServiceClient productServiceClient) {
        return new OrderCommandService(orderCommandPort, productServiceClient);
    }

    @Bean
    public OrderQueryService orderQueryService(OrderQueryPort orderQueryPort) {
        return new OrderQueryServiceImpl(orderQueryPort);
    }
}

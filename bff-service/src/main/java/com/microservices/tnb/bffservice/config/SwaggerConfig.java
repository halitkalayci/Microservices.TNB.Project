package com.microservices.tnb.bffservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bffOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BFF Service API")
                        .description("Backend For Frontend - Gateway Relayer with Session-Based Auth")
                        .version("1.0.0"));
    }
}

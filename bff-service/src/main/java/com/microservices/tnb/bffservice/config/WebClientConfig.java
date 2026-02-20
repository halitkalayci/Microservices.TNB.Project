package com.microservices.tnb.bffservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${bff.gateway-url}")
    private String gatewayUrl;

    @Value("${bff.identity-url}")
    private String identityUrl;

    @Bean(name = "gatewayWebClient")
    public WebClient gatewayWebClient() {
        return WebClient.builder()
                .baseUrl(gatewayUrl)
                .build();
    }

    @Bean(name = "identityWebClient")
    public WebClient identityWebClient() {
        HttpClient httpClient = HttpClient.create()
                .followRedirect(false);

        return WebClient.builder()
                .baseUrl(identityUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}

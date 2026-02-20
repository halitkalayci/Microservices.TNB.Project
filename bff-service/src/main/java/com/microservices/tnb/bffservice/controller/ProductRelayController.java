package com.microservices.tnb.bffservice.controller;

import com.microservices.tnb.bffservice.service.GatewayRelayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products Relay", description = "Relays product requests to Gateway")
public class ProductRelayController {

    private final GatewayRelayService gatewayRelayService;

    public ProductRelayController(GatewayRelayService gatewayRelayService) {
        this.gatewayRelayService = gatewayRelayService;
    }

    @GetMapping
    @Operation(summary = "Get paginated list of products (relayed via Gateway)")
    public Mono<ResponseEntity<String>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            WebSession session) {

        String path = String.format("/api/v1/products?page=%d&size=%d", page, size);
        return gatewayRelayService.relayGet(path, session)
                .map(body -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID (relayed via Gateway)")
    public Mono<ResponseEntity<String>> getProductById(
            @PathVariable String id,
            WebSession session) {

        String path = String.format("/api/v1/products/%s", id);
        return gatewayRelayService.relayGet(path, session)
                .map(body -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body));
    }
}

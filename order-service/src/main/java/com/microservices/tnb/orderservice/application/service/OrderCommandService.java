package com.microservices.tnb.orderservice.application.service;

import com.microservices.tnb.orderservice.application.mapper.OrderMapper;
import com.microservices.tnb.orderservice.domain.model.Order;
import com.microservices.tnb.orderservice.domain.port.OrderCommandPort;
import com.microservices.tnb.orderservice.dto.CreateOrderRequest;
import com.microservices.tnb.orderservice.dto.CreateOrderResponse;
import com.microservices.tnb.orderservice.dto.OrderItemRequest;
import com.microservices.tnb.orderservice.infrastructure.client.ProductServiceClient;
import com.microservices.tnb.orderservice.infrastructure.client.dto.ProductClientResponse;

import java.time.LocalDateTime;

import feign.FeignException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class OrderCommandService {

    private final OrderCommandPort orderCommandPort;
    private final ProductServiceClient productServiceClient;

    public OrderCommandService(OrderCommandPort orderCommandPort,
                               ProductServiceClient productServiceClient) {
        this.orderCommandPort = orderCommandPort;
        this.productServiceClient = productServiceClient;
    }

    @PreAuthorize("hasAuthority('Order.Create')")
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        validateProducts(request);

        Order order = OrderMapper.toDomainFromCreate(request);
        order.setUserId(getCurrentUserId());
        order.setCreatedAt(LocalDateTime.now());
        Order saved = orderCommandPort.save(order);
        return OrderMapper.toCreateOrderResponse(saved);
    }

    private void validateProducts(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        for (OrderItemRequest item : request.getItems()) {
            ProductClientResponse product;
            try {
                product = productServiceClient.getProductById(item.getProductId().toString());
            } catch (FeignException.NotFound e) {
                throw new IllegalArgumentException(
                        "Product not found: " + item.getProductId());
            } catch (FeignException e) {
                throw new RuntimeException(
                        "Failed to validate product: " + item.getProductId(), e);
            }

            if (product.getStock() == null || product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product: " + item.getProductId()
                                + ". Available: " + (product.getStock() != null ? product.getStock() : 0)
                                + ", Requested: " + item.getQuantity());
            }
        }
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaimAsString("userId");
        }
        return null;
    }
}

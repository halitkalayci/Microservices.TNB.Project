package com.microservices.tnb.orderservice.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GetOrderByIdResponse {

    private String id;
    private String userId;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;

    public GetOrderByIdResponse() {
    }

    public GetOrderByIdResponse(String id, String userId, LocalDateTime createdAt, List<OrderItemResponse> items) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}

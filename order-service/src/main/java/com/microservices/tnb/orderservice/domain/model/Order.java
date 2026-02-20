package com.microservices.tnb.orderservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private UUID id;
    private String userId;
    private LocalDateTime createdAt;
    private List<OrderDetail> items;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(UUID id, String userId, LocalDateTime createdAt, List<OrderDetail> items) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.items = items != null ? items : new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public List<OrderDetail> getItems() {
        return items;
    }

    public void setItems(List<OrderDetail> items) {
        this.items = items;
    }
}

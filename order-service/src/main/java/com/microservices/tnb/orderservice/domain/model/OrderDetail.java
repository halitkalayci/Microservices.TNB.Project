package com.microservices.tnb.orderservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderDetail {

    private UUID id;
    private UUID productId;
    private UUID orderId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderDetail() {
    }

    public OrderDetail(UUID id, UUID productId, UUID orderId, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

package com.microservices.tnb.orderservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemRequest {

    private UUID productId;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItemRequest() {
    }

    public OrderItemRequest(UUID productId, int quantity, BigDecimal unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
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

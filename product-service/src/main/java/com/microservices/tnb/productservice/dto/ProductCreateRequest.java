package com.microservices.tnb.productservice.dto;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal unitPrice;
    private Integer stock;
    private String sku;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(String name, BigDecimal unitPrice, Integer stock, String sku) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}

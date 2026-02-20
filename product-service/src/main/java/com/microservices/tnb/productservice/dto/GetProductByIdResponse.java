package com.microservices.tnb.productservice.dto;

import java.math.BigDecimal;

public class GetProductByIdResponse {

    private String id;
    private String name;
    private BigDecimal unitPrice;
    private Integer stock;
    private String sku;

    public GetProductByIdResponse() {
    }

    public GetProductByIdResponse(String id, String name, BigDecimal unitPrice, Integer stock, String sku) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
        this.sku = sku;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

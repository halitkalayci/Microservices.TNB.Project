package com.microservices.tnb.productservice.dto;

public class DeleteProductResponse {

    private String id;

    public DeleteProductResponse() {
    }

    public DeleteProductResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

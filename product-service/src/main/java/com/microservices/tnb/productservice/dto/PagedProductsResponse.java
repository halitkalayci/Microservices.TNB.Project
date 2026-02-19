package com.microservices.tnb.productservice.dto;

import java.util.List;

public class PagedProductsResponse {

    private List<ProductResponse> items;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    public PagedProductsResponse() {
    }

    public PagedProductsResponse(List<ProductResponse> items, Integer page, Integer size, Long totalElements, Integer totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<ProductResponse> getItems() {
        return items;
    }

    public void setItems(List<ProductResponse> items) {
        this.items = items;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}

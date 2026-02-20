package com.microservices.tnb.orderservice.dto;

import java.util.List;

public class PagedOrdersResponse {

    private List<OrderResponse> items;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;

    public PagedOrdersResponse() {
    }

    public PagedOrdersResponse(List<OrderResponse> items, Integer page, Integer size, Long totalElements, Integer totalPages) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<OrderResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderResponse> items) {
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

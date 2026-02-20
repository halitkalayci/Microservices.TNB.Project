package com.microservices.tnb.productservice.application.service;

import com.microservices.tnb.productservice.application.mapper.ProductMapper;
import com.microservices.tnb.productservice.domain.port.ProductQueryPort;
import com.microservices.tnb.productservice.dto.GetProductByIdResponse;
import com.microservices.tnb.productservice.dto.PagedProductsResponse;
import com.microservices.tnb.productservice.dto.ProductResponse;
import com.microservices.tnb.productservice.service.ProductQueryService;

import java.util.List;
import java.util.Optional;

public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductQueryPort productQueryPort;

    public ProductQueryServiceImpl(ProductQueryPort productQueryPort) {
        this.productQueryPort = productQueryPort;
    }

    @Override
    public PagedProductsResponse getProducts(Integer page, Integer size) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;

        long totalElements = productQueryPort.countAll();
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / (double) pageSize) : 0;

        List<ProductResponse> items = ProductMapper.toProductResponseList(
                productQueryPort.findAll(pageNumber, pageSize)
        );

        return new PagedProductsResponse(
                items,
                pageNumber,
                pageSize,
                totalElements,
                totalPages
        );
    }

    @Override
    public Optional<GetProductByIdResponse> getProductById(String id) {
        java.util.UUID uuid = java.util.UUID.fromString(id);
        return productQueryPort.findById(uuid)
                .map(ProductMapper::toGetProductByIdResponse);
    }
}

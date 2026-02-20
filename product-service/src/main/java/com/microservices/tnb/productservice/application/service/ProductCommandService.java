package com.microservices.tnb.productservice.application.service;

import com.microservices.tnb.productservice.application.mapper.ProductMapper;
import com.microservices.tnb.productservice.domain.model.Product;
import com.microservices.tnb.productservice.domain.port.ProductCommandPort;
import com.microservices.tnb.productservice.dto.CreateProductResponse;
import com.microservices.tnb.productservice.dto.ProductCreateRequest;
import com.microservices.tnb.productservice.dto.ProductUpdateRequest;
import com.microservices.tnb.productservice.dto.UpdateProductResponse;

import java.util.Optional;
import java.util.UUID;

public class ProductCommandService {

    private final ProductCommandPort productCommandPort;

    public ProductCommandService(ProductCommandPort productCommandPort) {
        this.productCommandPort = productCommandPort;
    }

    public CreateProductResponse createProduct(ProductCreateRequest request) {
        Product toCreate = ProductMapper.toDomainFromCreate(request);
        Product created = productCommandPort.create(toCreate);
        return ProductMapper.toCreateProductResponse(created);
    }

    public Optional<UpdateProductResponse> updateProduct(String id, ProductUpdateRequest request) {
        UUID uuid = UUID.fromString(id);
        Product toUpdate = ProductMapper.toDomainFromUpdate(uuid, request);
        return productCommandPort.update(toUpdate)
                .map(ProductMapper::toUpdateProductResponse);
    }

    public boolean deleteProduct(String id) {
        UUID uuid = UUID.fromString(id);
        return productCommandPort.deleteById(uuid);
    }
}

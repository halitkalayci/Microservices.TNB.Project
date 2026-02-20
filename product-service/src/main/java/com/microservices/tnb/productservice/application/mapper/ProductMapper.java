package com.microservices.tnb.productservice.application.mapper;

import com.microservices.tnb.productservice.domain.model.Product;
import com.microservices.tnb.productservice.dto.CreateProductResponse;
import com.microservices.tnb.productservice.dto.GetProductByIdResponse;
import com.microservices.tnb.productservice.dto.ProductCreateRequest;
import com.microservices.tnb.productservice.dto.ProductResponse;
import com.microservices.tnb.productservice.dto.ProductUpdateRequest;
import com.microservices.tnb.productservice.dto.UpdateProductResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductMapper {

    private ProductMapper() {
    }

    public static Product toDomainFromCreate(ProductCreateRequest request) {
        if (request == null) {
            return null;
        }
        return new Product(
                null,
                request.getName(),
                request.getUnitPrice(),
                request.getStock(),
                request.getSku()
        );
    }

    public static Product toDomainFromUpdate(UUID id, ProductUpdateRequest request) {
        if (request == null) {
            return null;
        }
        return new Product(
                id,
                request.getName(),
                request.getUnitPrice(),
                request.getStock(),
                request.getSku()
        );
    }

    public static CreateProductResponse toCreateProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new CreateProductResponse(
                product.getId().toString(),
                product.getName(),
                product.getUnitPrice(),
                product.getStock(),
                product.getSku()
        );
    }

    public static GetProductByIdResponse toGetProductByIdResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new GetProductByIdResponse(
            product.getId().toString(),
            product.getName(),
            product.getUnitPrice(),
            product.getStock(),
            product.getSku()
        );
    }

    public static UpdateProductResponse toUpdateProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new UpdateProductResponse(
                product.getId().toString(),
                product.getName(),
                product.getUnitPrice(),
                product.getStock(),
                product.getSku()
        );
    }

    public static ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductResponse(
                product.getId().toString(),
                product.getName(),
                product.getUnitPrice(),
                product.getStock(),
                product.getSku()
        );
    }

    public static java.util.List<ProductResponse> toProductResponseList(List<Product> products) {
        if (products == null) {
            return java.util.Collections.emptyList();
        }
        return products.stream()
                .map(ProductMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}

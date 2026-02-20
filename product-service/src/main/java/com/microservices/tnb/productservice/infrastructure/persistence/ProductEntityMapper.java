package com.microservices.tnb.productservice.infrastructure.persistence;

import com.microservices.tnb.productservice.domain.model.Product;

public class ProductEntityMapper {

    private ProductEntityMapper() {
    }

    public static ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getUnitPrice(),
                product.getStock(),
                product.getSku()
        );
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getUnitPrice(),
                entity.getStock(),
                entity.getSku()
        );
    }
}

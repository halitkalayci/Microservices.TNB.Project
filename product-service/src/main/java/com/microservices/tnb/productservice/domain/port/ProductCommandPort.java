package com.microservices.tnb.productservice.domain.port;

import com.microservices.tnb.productservice.domain.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductCommandPort {

    Product create(Product product);

    Optional<Product> update(Product product);

    boolean deleteById(UUID id);
}

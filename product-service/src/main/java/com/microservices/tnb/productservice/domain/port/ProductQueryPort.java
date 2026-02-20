package com.microservices.tnb.productservice.domain.port;

import com.microservices.tnb.productservice.domain.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductQueryPort {

    Optional<Product> findById(UUID id);

    List<Product> findAll(int page, int size);

    long countAll();
}

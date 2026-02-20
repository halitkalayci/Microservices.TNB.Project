package com.microservices.tnb.productservice.infrastructure.persistence;

import com.microservices.tnb.productservice.domain.model.Product;
import com.microservices.tnb.productservice.domain.port.ProductCommandPort;
import com.microservices.tnb.productservice.domain.port.ProductQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements ProductCommandPort, ProductQueryPort {

    private final SpringDataProductRepository repository;

    public ProductRepositoryAdapter(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product create(Product product) {
        ProductEntity entity = ProductEntityMapper.toEntity(product);
        ProductEntity saved = repository.save(entity);
        return ProductEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> update(Product product) {
        if (product.getId() == null) {
            return Optional.empty();
        }
        Optional<ProductEntity> existingOpt = repository.findById(product.getId());
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }
        ProductEntity entity = ProductEntityMapper.toEntity(product);
        entity.setCreatedBy(existingOpt.get().getCreatedBy());
        ProductEntity saved = repository.save(entity);
        return Optional.ofNullable(ProductEntityMapper.toDomain(saved));
    }

    @Override
    public boolean deleteById(UUID id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id)
                .map(ProductEntityMapper::toDomain);
    }

    @Override
    public List<Product> findAll(int page, int size) {
        Page<ProductEntity> result = repository.findAll(PageRequest.of(page, size));
        return result.getContent().stream()
                .map(ProductEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countAll() {
        return repository.count();
    }
}

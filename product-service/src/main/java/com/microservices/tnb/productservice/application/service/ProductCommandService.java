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

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class ProductCommandService {

    private final ProductCommandPort productCommandPort;

    public ProductCommandService(ProductCommandPort productCommandPort) {
        this.productCommandPort = productCommandPort;
    }

    @PreAuthorize("hasAuthority('Product.Create')")
    public CreateProductResponse createProduct(ProductCreateRequest request) {
        Product toCreate = ProductMapper.toDomainFromCreate(request);
        toCreate.setCreatedBy(getCurrentUserId());
        Product created = productCommandPort.create(toCreate);
        return ProductMapper.toCreateProductResponse(created);
    }

    @PreAuthorize("@productSecurityService.isOwner(#id)")
    public Optional<UpdateProductResponse> updateProduct(String id, ProductUpdateRequest request) {
        UUID uuid = UUID.fromString(id);
        Product toUpdate = ProductMapper.toDomainFromUpdate(uuid, request);
        return productCommandPort.update(toUpdate)
                .map(ProductMapper::toUpdateProductResponse);
    }

    @PreAuthorize("@productSecurityService.isOwner(#id)")
    public boolean deleteProduct(String id) {
        UUID uuid = UUID.fromString(id);
        return productCommandPort.deleteById(uuid);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaimAsString("userId");
        }
        return null;
    }
}

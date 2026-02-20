package com.microservices.tnb.productservice.infrastructure.security;

import com.microservices.tnb.productservice.domain.model.Product;
import com.microservices.tnb.productservice.domain.port.ProductQueryPort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component("productSecurityService")
public class ProductSecurityService {

    private final ProductQueryPort productQueryPort;

    public ProductSecurityService(ProductQueryPort productQueryPort) {
        this.productQueryPort = productQueryPort;
    }

    public boolean isOwner(String productId) {
        String currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }

        Optional<Product> product = productQueryPort.findById(UUID.fromString(productId));
        if (product.isEmpty()) {
            return false;
        }

        String createdBy = product.get().getCreatedBy();
        if (createdBy == null) {
            return false;
        }

        return createdBy.equals(currentUserId);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken().getClaimAsString("userId");
        }
        return null;
    }
}

package com.microservices.tnb.productservice.api;

import com.microservices.tnb.productservice.application.service.ProductCommandService;
import com.microservices.tnb.productservice.dto.CreateProductResponse;
import com.microservices.tnb.productservice.dto.DeleteProductResponse;
import com.microservices.tnb.productservice.dto.GetProductByIdResponse;
import com.microservices.tnb.productservice.dto.PagedProductsResponse;
import com.microservices.tnb.productservice.dto.ProductCreateRequest;
import com.microservices.tnb.productservice.dto.ProductUpdateRequest;
import com.microservices.tnb.productservice.dto.UpdateProductResponse;
import com.microservices.tnb.productservice.service.ProductQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;

    public ProductController(ProductQueryService productQueryService,
                             ProductCommandService productCommandService) {
        this.productQueryService = productQueryService;
        this.productCommandService = productCommandService;
    }

    @GetMapping
    public ResponseEntity<PagedProductsResponse> getProducts(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        if (page != null && page < 0) {
            return ResponseEntity.badRequest().build();
        }

        if (size != null && size <= 0) {
            return ResponseEntity.badRequest().build();
        }

        PagedProductsResponse response = productQueryService.getProducts(page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody ProductCreateRequest request) {
        CreateProductResponse response = productCommandService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductByIdResponse> getProductById(@PathVariable("id") String id) {
        try {
            java.util.UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return productQueryService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable("id") String id,
                                                               @RequestBody ProductUpdateRequest request) {
        try {
            java.util.UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return productCommandService.updateProduct(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteProductResponse> deleteProduct(@PathVariable("id") String id) {
        java.util.UUID uuid;
        try {
            uuid = java.util.UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        boolean deleted = productCommandService.deleteProduct(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        DeleteProductResponse response = new DeleteProductResponse(uuid.toString());
        return ResponseEntity.ok(response);
    }
}

package com.microservices.tnb.productservice.api;

import com.microservices.tnb.productservice.dto.PagedProductsResponse;
import com.microservices.tnb.productservice.service.ProductQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {


    public ProductController() {
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

        return ResponseEntity.ok(new PagedProductsResponse());
    }
}

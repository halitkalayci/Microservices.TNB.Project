package com.microservices.tnb.productservice.service;

import com.microservices.tnb.productservice.dto.GetProductByIdResponse;
import com.microservices.tnb.productservice.dto.PagedProductsResponse;

import java.util.Optional;

public interface ProductQueryService {

    PagedProductsResponse getProducts(Integer page, Integer size);

    Optional<GetProductByIdResponse> getProductById(String id);
}

package com.microservices.tnb.productservice.service;

import com.microservices.tnb.productservice.dto.PagedProductsResponse;

public interface ProductQueryService {

    PagedProductsResponse getProducts(Integer page, Integer size);
}

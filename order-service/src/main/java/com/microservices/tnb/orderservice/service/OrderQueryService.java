package com.microservices.tnb.orderservice.service;

import com.microservices.tnb.orderservice.dto.GetOrderByIdResponse;
import com.microservices.tnb.orderservice.dto.PagedOrdersResponse;

import java.util.Optional;

public interface OrderQueryService {

    PagedOrdersResponse getOrders(Integer page, Integer size);

    Optional<GetOrderByIdResponse> getOrderById(String id);
}

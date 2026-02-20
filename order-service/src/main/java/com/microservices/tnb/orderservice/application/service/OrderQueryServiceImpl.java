package com.microservices.tnb.orderservice.application.service;

import com.microservices.tnb.orderservice.application.mapper.OrderMapper;
import com.microservices.tnb.orderservice.domain.port.OrderQueryPort;
import com.microservices.tnb.orderservice.dto.GetOrderByIdResponse;
import com.microservices.tnb.orderservice.dto.OrderResponse;
import com.microservices.tnb.orderservice.dto.PagedOrdersResponse;
import com.microservices.tnb.orderservice.service.OrderQueryService;

import java.util.List;
import java.util.Optional;

public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderQueryPort orderQueryPort;

    public OrderQueryServiceImpl(OrderQueryPort orderQueryPort) {
        this.orderQueryPort = orderQueryPort;
    }

    @Override
    public PagedOrdersResponse getOrders(Integer page, Integer size) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;

        long totalElements = orderQueryPort.countAll();
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / (double) pageSize) : 0;

        List<OrderResponse> items = OrderMapper.toOrderResponseList(
                orderQueryPort.findAll(pageNumber, pageSize)
        );

        return new PagedOrdersResponse(
                items,
                pageNumber,
                pageSize,
                totalElements,
                totalPages
        );
    }

    @Override
    public Optional<GetOrderByIdResponse> getOrderById(String id) {
        java.util.UUID uuid = java.util.UUID.fromString(id);
        return orderQueryPort.findById(uuid)
                .map(OrderMapper::toGetOrderByIdResponse);
    }
}

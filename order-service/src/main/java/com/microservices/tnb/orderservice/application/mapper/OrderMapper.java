package com.microservices.tnb.orderservice.application.mapper;

import com.microservices.tnb.orderservice.domain.model.Order;
import com.microservices.tnb.orderservice.domain.model.OrderDetail;
import com.microservices.tnb.orderservice.dto.CreateOrderRequest;
import com.microservices.tnb.orderservice.dto.CreateOrderResponse;
import com.microservices.tnb.orderservice.dto.GetOrderByIdResponse;
import com.microservices.tnb.orderservice.dto.OrderItemRequest;
import com.microservices.tnb.orderservice.dto.OrderItemResponse;
import com.microservices.tnb.orderservice.dto.OrderResponse;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order toDomainFromCreate(CreateOrderRequest request) {
        if (request == null) {
            return null;
        }

        Order order = new Order();

        if (request.getItems() != null) {
            List<OrderDetail> details = request.getItems().stream()
                    .map(OrderMapper::toOrderDetail)
                    .collect(Collectors.toList());
            order.setItems(details);
        }

        return order;
    }

    private static OrderDetail toOrderDetail(OrderItemRequest itemRequest) {
        if (itemRequest == null) {
            return null;
        }

        OrderDetail detail = new OrderDetail();
        detail.setProductId(itemRequest.getProductId());
        detail.setQuantity(itemRequest.getQuantity());
        detail.setUnitPrice(itemRequest.getUnitPrice());

        return detail;
    }

    public static CreateOrderResponse toCreateOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        return new CreateOrderResponse(
                order.getId().toString(),
                order.getUserId(),
                order.getCreatedAt(),
                toOrderItemResponseList(order.getItems())
        );
    }

    public static GetOrderByIdResponse toGetOrderByIdResponse(Order order) {
        if (order == null) {
            return null;
        }

        return new GetOrderByIdResponse(
                order.getId().toString(),
                order.getUserId(),
                order.getCreatedAt(),
                toOrderItemResponseList(order.getItems())
        );
    }

    public static OrderResponse toOrderResponse(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderResponse(
                order.getId().toString(),
                order.getUserId(),
                order.getCreatedAt(),
                toOrderItemResponseList(order.getItems())
        );
    }

    public static List<OrderResponse> toOrderResponseList(List<Order> orders) {
        if (orders == null) {
            return Collections.emptyList();
        }

        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    private static List<OrderItemResponse> toOrderItemResponseList(List<OrderDetail> items) {
        if (items == null) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(OrderMapper::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    private static OrderItemResponse toOrderItemResponse(OrderDetail detail) {
        if (detail == null) {
            return null;
        }

        return new OrderItemResponse(
                detail.getId() != null ? detail.getId().toString() : null,
                detail.getProductId() != null ? detail.getProductId().toString() : null,
                detail.getQuantity(),
                detail.getUnitPrice()
        );
    }
}

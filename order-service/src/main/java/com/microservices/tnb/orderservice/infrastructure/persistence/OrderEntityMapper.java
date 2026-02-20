package com.microservices.tnb.orderservice.infrastructure.persistence;

import com.microservices.tnb.orderservice.domain.model.Order;
import com.microservices.tnb.orderservice.domain.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderEntityMapper {

    private OrderEntityMapper() {
    }

    public static OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUserId(order.getUserId());
        entity.setCreatedAt(order.getCreatedAt());

        if (order.getItems() != null) {
            List<OrderDetailEntity> detailEntities = order.getItems().stream()
                    .map(detail -> toDetailEntity(detail, entity))
                    .collect(Collectors.toList());
            entity.setItems(detailEntities);
        } else {
            entity.setItems(new ArrayList<>());
        }

        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        Order order = new Order();
        order.setId(entity.getId());
        order.setUserId(entity.getUserId());
        order.setCreatedAt(entity.getCreatedAt());

        if (entity.getItems() != null) {
            List<OrderDetail> details = entity.getItems().stream()
                    .map(OrderEntityMapper::toDetailDomain)
                    .collect(Collectors.toList());
            order.setItems(details);
        } else {
            order.setItems(new ArrayList<>());
        }

        return order;
    }

    private static OrderDetailEntity toDetailEntity(OrderDetail detail, OrderEntity orderEntity) {
        if (detail == null) {
            return null;
        }

        OrderDetailEntity entity = new OrderDetailEntity();
        entity.setId(detail.getId());
        entity.setProductId(detail.getProductId());
        entity.setOrder(orderEntity);
        entity.setQuantity(detail.getQuantity());
        entity.setUnitPrice(detail.getUnitPrice());

        return entity;
    }

    private static OrderDetail toDetailDomain(OrderDetailEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OrderDetail(
                entity.getId(),
                entity.getProductId(),
                entity.getOrder() != null ? entity.getOrder().getId() : null,
                entity.getQuantity(),
                entity.getUnitPrice()
        );
    }
}

package com.microservices.tnb.orderservice.domain.port;

import com.microservices.tnb.orderservice.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderQueryPort {

    Optional<Order> findById(UUID id);

    List<Order> findAll(int page, int size);

    long countAll();
}

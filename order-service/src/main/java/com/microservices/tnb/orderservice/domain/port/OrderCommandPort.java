package com.microservices.tnb.orderservice.domain.port;

import com.microservices.tnb.orderservice.domain.model.Order;

public interface OrderCommandPort {

    Order save(Order order);
}

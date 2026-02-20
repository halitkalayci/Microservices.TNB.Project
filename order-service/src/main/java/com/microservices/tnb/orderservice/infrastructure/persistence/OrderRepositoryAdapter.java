package com.microservices.tnb.orderservice.infrastructure.persistence;

import com.microservices.tnb.orderservice.domain.model.Order;
import com.microservices.tnb.orderservice.domain.port.OrderCommandPort;
import com.microservices.tnb.orderservice.domain.port.OrderQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderCommandPort, OrderQueryPort {

    private final SpringDataOrderRepository repository;

    public OrderRepositoryAdapter(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = OrderEntityMapper.toEntity(order);
        OrderEntity saved = repository.save(entity);
        return OrderEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return repository.findById(id)
                .map(OrderEntityMapper::toDomain);
    }

    @Override
    public List<Order> findAll(int page, int size) {
        Page<OrderEntity> result = repository.findAll(PageRequest.of(page, size));
        return result.getContent().stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countAll() {
        return repository.count();
    }
}

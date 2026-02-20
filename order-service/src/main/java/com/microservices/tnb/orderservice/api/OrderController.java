package com.microservices.tnb.orderservice.api;

import com.microservices.tnb.orderservice.application.service.OrderCommandService;
import com.microservices.tnb.orderservice.dto.CreateOrderRequest;
import com.microservices.tnb.orderservice.dto.CreateOrderResponse;
import com.microservices.tnb.orderservice.dto.GetOrderByIdResponse;
import com.microservices.tnb.orderservice.dto.PagedOrdersResponse;
import com.microservices.tnb.orderservice.service.OrderQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    public OrderController(OrderQueryService orderQueryService,
                           OrderCommandService orderCommandService) {
        this.orderQueryService = orderQueryService;
        this.orderCommandService = orderCommandService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = orderCommandService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Order.Read')")
    public ResponseEntity<GetOrderByIdResponse> getOrderById(@PathVariable("id") String id) {
        try {
            java.util.UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

        return orderQueryService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('Order.Read')")
    public ResponseEntity<PagedOrdersResponse> getOrders(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size) {

        if (page != null && page < 0) {
            return ResponseEntity.badRequest().build();
        }

        if (size != null && size <= 0) {
            return ResponseEntity.badRequest().build();
        }

        PagedOrdersResponse response = orderQueryService.getOrders(page, size);
        return ResponseEntity.ok(response);
    }
}

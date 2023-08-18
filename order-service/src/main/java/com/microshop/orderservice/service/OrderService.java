package com.microshop.orderservice.service;

import com.microshop.orderservice.dto.OrderDto;
import com.microshop.orderservice.entity.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface OrderService {

    Mono<Order> placeOrder(OrderDto orderDto);

    Mono<Order> getOrderDetails(Long orderId);

    Flux<Order> getOrderHistory(Long customerId);
}

package se.isai.microservices.core.order.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.isai.microservices.core.order.dto.Order;

import java.util.List;

public interface OrderService {
    Flux<Order> getOrders(String userId);
    Mono<Order> createOrder(Order order);

    Mono<Void> deleteOrder(Long orderId);
}

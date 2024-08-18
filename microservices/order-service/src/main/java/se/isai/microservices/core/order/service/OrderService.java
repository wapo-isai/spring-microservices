package se.isai.microservices.core.order.service;

import se.isai.microservices.core.order.dto.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders(String userId);
    Order createOrder(Order order);

    void deleteOrder(Long orderId);
}

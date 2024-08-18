package se.isai.microservices.core.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.isai.microservices.core.order.dto.Order;
import se.isai.microservices.core.order.persistence.OrderEntity;
import se.isai.microservices.core.order.persistence.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Override
    public List<Order> getOrders(String userId) {
        List<OrderEntity> orderEntities = orderRepository.findAllByUserId(userId);

        List<Order> orderList = new ArrayList<>();

        if(orderList.size() == 0) {
            return orderList;
        }

        for(OrderEntity entity : orderEntities) {
            Order order = new Order(
                    entity.getUserId(),
                    entity.getOrderNumber(),
                    entity.getTotalPrice(),
                    entity.getProductIds());

            orderList.add(order);
        }

        return orderList;
    }

    @Override
    public Order createOrder(Order order) {
        order.setOrderNumber(UUID.randomUUID().toString());

        OrderEntity orderEntity = new OrderEntity(
                order.getUserId(),
                order.getOrderNumber(),
                order.getTotalPrice(),
                order.getProductIds()
        );

        OrderEntity storedOrderEntity = orderRepository.save(orderEntity);

        Order returnValue = new Order(
                storedOrderEntity.getUserId(),
                storedOrderEntity.getOrderNumber(),
                storedOrderEntity.getTotalPrice(),
                storedOrderEntity.getProductIds()
        );

        return returnValue;
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}

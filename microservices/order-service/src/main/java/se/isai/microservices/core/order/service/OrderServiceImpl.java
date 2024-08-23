package se.isai.microservices.core.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.isai.microservices.core.order.dto.Order;
import se.isai.microservices.core.order.persistence.OrderEntity;
import se.isai.microservices.core.order.persistence.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final Scheduler jdbcScheduler;

    OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, OrderRepository orderRepository) {
        this.jdbcScheduler = jdbcScheduler;
        this.orderRepository = orderRepository;
    }

    @Override
    public Flux<Order> getOrders(String userId) {
        return Mono.fromCallable(() -> {
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
        }).flatMapMany(Flux::fromIterable)
                .log(LOG.getName(), Level.FINE)
                .subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Order> createOrder(Order order) {
        return Mono.fromCallable(() -> {
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
        }).subscribeOn(jdbcScheduler);
    }

    @Override
    public Mono<Void> deleteOrder(Long orderId) {
        return Mono.fromRunnable(() -> {
            orderRepository.deleteById(orderId);
        }).subscribeOn(jdbcScheduler).then();
    }
}

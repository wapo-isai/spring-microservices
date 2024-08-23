package se.isai.microservices.core.order.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.isai.microservices.core.order.dto.Order;
import se.isai.microservices.core.order.service.OrderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/{userId}")
    public Flux<Order> getOrders(@PathVariable("userId") String userId) {
        return orderService.getOrders(userId);
    }
}

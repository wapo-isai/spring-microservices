package se.isai.microservices.core.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.isai.microservices.core.order.dto.Event;
import se.isai.microservices.core.order.dto.Order;
import se.isai.microservices.core.order.exceptions.EventProcessingException;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final OrderService orderService;

    @Autowired
    MessageProcessorConfig(OrderService orderService) {
        this.orderService = orderService;
    }

    @Bean
    public Consumer<Event<Long, Order>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Order order = event.getData();
                    orderService.createOrder(order);
                    break;

                case DELETE:
                    Long orderId = event.getKey();
                    orderService.deleteOrder(orderId);
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }

            LOG.info("Message processing done!");
        };
    }
}


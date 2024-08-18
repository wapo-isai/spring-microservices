package se.isai.microservices.core.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.isai.microservices.core.product.dto.Event;
import se.isai.microservices.core.product.dto.Product;
import se.isai.microservices.core.product.exceptions.EventProcessingException;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {
    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final ProductService productService;

    @Autowired
    MessageProcessorConfig(ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public Consumer<Event<String, Product>> messageProcessor() {
        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Product product = event.getData();
                    LOG.info("Create product with ID: {}", product.getProductId());
                    productService.saveProduct(product);
                    break;

                case DELETE:
                    String productId = event.getKey();
                    LOG.info("Delete product with ProductID: {}", productId);
                    productService.deleteProduct(productId);
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

package se.isai.microservices.composite.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.isai.microservices.composite.product.web.ProductCompositeIntegration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class HealthCheckConfiguration {

    @Autowired
    ProductCompositeIntegration integration;

    @Bean
    HealthContributor coreServices() {

        final Map<String, HealthIndicator> registry = new LinkedHashMap<>();

        registry.put("product", () -> integration.getProductHealth());
        registry.put("recommendation", () -> integration.getOrderHealth());
        registry.put("review", () -> integration.getUserHealth());

        return CompositeHealthContributor.fromMap(registry);
    }
}

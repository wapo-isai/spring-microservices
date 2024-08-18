package se.isai.microservices.composite.product.web;

import static org.springframework.http.HttpMethod.GET;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import se.isai.microservices.composite.product.dto.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comp")
public class ProductCompositeIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;

    private final String productServiceUrl;
    private final String orderServiceUrl;
    private final String userServiceUrl;

    @Autowired
    private StreamBridge streamBridge;

    @Autowired
    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") int productServicePort,
            @Value("${app.order-service.host}") String orderServiceHost,
            @Value("${app.order-service.port}") int orderServicePort,
            @Value("${app.user-service.host}") String userServiceHost,
            @Value("${app.user-service.port}") int userServicePort
    ) {

        this.restTemplate = restTemplate;
        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product";
        orderServiceUrl = "http://" + orderServiceHost + ":" + orderServicePort + "/order";
        userServiceUrl = "http://" + userServiceHost + ":" + userServicePort + "/user";
    }

    @GetMapping(value = "/product/{productId}")
    public Product getProduct(@PathVariable String productId) {
        try {
            String url = productServiceUrl + "/" + productId;

            Product product = restTemplate.getForObject(url, Product.class);
            return product;
        } catch (Exception ex) {
            LOG.debug("Exception: " + ex.getMessage());
            return null;
        }
    }

    @GetMapping(value = "/product/list")
    public List<Product> getProducts() {
        try {
            String url = productServiceUrl + "/list";

            LOG.debug("Will call the getRecommendations API on URL: {}", url);
            List<Product> products = restTemplate
                    .exchange(url, GET, null, new ParameterizedTypeReference<List<Product>>() {})
                    .getBody();

            LOG.debug("Found {} products", products.size());
            return products;

        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting products, return zero products: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @PostMapping(value = "/product")
    public Product createProduct(Product body) {
        sendMessage("products-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
        return body;
    }

    @DeleteMapping(value="/product/{productId}")
    public void deleteProduct(@PathVariable String productId) {
        sendMessage("products-out-0", new Event(Event.Type.DELETE, productId, null));
    }

    @GetMapping(value="/order/{userId}")
    public List<Order> getUserOrders(@PathVariable("userId") String userId) {
        try {
            String url = orderServiceUrl + "/" + userId;

            List<Order> orders = restTemplate
                    .exchange(url, GET, null, new ParameterizedTypeReference<List<Order>>() {})
                    .getBody();

            LOG.debug("Found {} orders", orders.size());
            return orders;
        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting products, return zero products: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @PostMapping(value="/order")
    public void createOrderForUser(@RequestBody Order order) {
        sendMessage("orders-out-0", new Event(Event.Type.CREATE, order.getUserId(), order));
    }

    @DeleteMapping(value="/order/{orderNumber}")
    public void deleteUserOrder(@PathVariable("orderNumber") Long orderId) {
        sendMessage("orders-out-0", new Event(Event.Type.DELETE, orderId, null));
    }

    @GetMapping(value = "/user/{userId}")
    public UserResponseModel login(@PathVariable("userId") String userId) {
        try {
            String url = userServiceUrl + "/" + userId;
            UserResponseModel user = restTemplate.getForObject(url, UserResponseModel.class);
            return user;
        } catch (Exception ex) {
            LOG.debug("Exception: " + ex.getMessage());
            return null;
        }
    }

    @PostMapping(value="/user/register")
    public void register(@Valid @RequestBody User user) {
        sendMessage("users-out-0", new Event(Event.Type.CREATE, user.getUserId(), user));
    }

    @DeleteMapping(value="/user/{userId}")
    public void unregisterUser(@PathVariable("userId") String userId) {
        sendMessage("users-out-0", new Event(Event.Type.DELETE, userId, null));
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }

    public Health getProductHealth() {
        return getHealth(productServiceUrl);
    }

    public Health getOrderHealth() {
        return getHealth(orderServiceUrl);
    }

    public Health getUserHealth() {
        return getHealth(userServiceUrl);
    }

    private Health getHealth(String url) {
        url += "/actuator/health";
        LOG.debug("Will call the Health API on URL: {}", url);
        String status = restTemplate.getForObject(url, String.class);

        if(status.equals("UP")){
            return Health.up().build();
        }

        return Health.down().build();
    }
}

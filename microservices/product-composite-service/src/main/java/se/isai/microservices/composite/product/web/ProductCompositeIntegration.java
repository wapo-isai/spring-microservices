package se.isai.microservices.composite.product.web;

import static java.util.logging.Level.FINE;

import static reactor.core.publisher.Flux.empty;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import se.isai.microservices.composite.product.dto.*;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@RestController
@RequestMapping("/comp")
public class ProductCompositeIntegration {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final WebClient webClient;

    private final String productServiceUrl;
    private final String orderServiceUrl;
    private final String userServiceUrl;

    private StreamBridge streamBridge;

    private final Scheduler publishEventScheduler;

    @Autowired
    public ProductCompositeIntegration(
            @Qualifier("publishEventScheduler") Scheduler publishEventScheduler,
            WebClient.Builder webClient,
            StreamBridge streamBridge,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") int productServicePort,
            @Value("${app.order-service.host}") String orderServiceHost,
            @Value("${app.order-service.port}") int orderServicePort,
            @Value("${app.user-service.host}") String userServiceHost,
            @Value("${app.user-service.port}") int userServicePort
    ) {
        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClient.build();
        this.streamBridge = streamBridge;

        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product";
        orderServiceUrl = "http://" + orderServiceHost + ":" + orderServicePort + "/order";
        userServiceUrl = "http://" + userServiceHost + ":" + userServicePort + "/user";
    }

    @GetMapping(value = "/product/{productId}")
    public Mono<Product> getProduct(@PathVariable String productId) {
            String url = productServiceUrl + "/" + productId;

            return webClient.get().uri(url).retrieve().bodyToMono(Product.class).log(LOG.getName(), FINE).onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
    }

    @GetMapping(value = "/product/list")
    public Flux<Product> getProducts() {
        String url = productServiceUrl + "/list";

        return webClient.get().uri(url).retrieve().bodyToFlux(Product.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }

    @PostMapping(value = "/product")
    public Mono<Product> createProduct(@RequestBody Product body) {
        return Mono.fromCallable(() -> {
            sendMessage("products-out-0", new Event(Event.Type.CREATE, body.getProductId(), body));
            return body;
        }).subscribeOn(publishEventScheduler);
    }

    @DeleteMapping(value="/product/{productId}")
    public Mono<Void> deleteProduct(@PathVariable String productId) {
        return Mono.fromRunnable(() -> sendMessage("products-out-0", new Event(Event.Type.DELETE, productId, null)))
                .subscribeOn(publishEventScheduler).then();
    }

    @GetMapping(value="/order/{userId}")
    public Flux<Order> getUserOrders(@PathVariable("userId") String userId) {

            String url = orderServiceUrl + "/" + userId;

            return webClient.get().uri(url).retrieve().bodyToFlux(Order.class).log(LOG.getName(), FINE).onErrorResume(error -> empty());
    }

    @PostMapping(value="/order")
    public Mono<Order> createOrderForUser(@RequestBody Order order) {
        return Mono.fromCallable(() -> {
            sendMessage("orders-out-0", new Event(Event.Type.CREATE, order.getUserId(), order));
            return order;
        }).subscribeOn(publishEventScheduler);
    }

    @DeleteMapping(value="/order/{orderNumber}")
    public Mono<Void> deleteUserOrder(@PathVariable("orderNumber") Long orderId) {
        return Mono.fromRunnable(() -> sendMessage("orders-out-0", new Event(Event.Type.DELETE, orderId, null)))
                .subscribeOn(publishEventScheduler).then();
    }

    @GetMapping(value = "/user/{userId}")
    public Mono<User> login(@PathVariable("userId") String userId) {
        String url = userServiceUrl + "/" + userId;
        return webClient.get().uri(url).retrieve().bodyToMono(User.class).log(LOG.getName(), FINE).onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
    }

    @PostMapping(value="/user/register")
    public Mono<User> register(@Valid @RequestBody User user) {
        return Mono.fromCallable(() -> {
            sendMessage("users-out-0", new Event(Event.Type.CREATE, user.getUserId(), user));
            return user;
        }).subscribeOn(publishEventScheduler);
    }

    @DeleteMapping(value="/user/{userId}")
    public Mono<Void> unregisterUser(@PathVariable("userId") String userId) {
        return Mono.fromRunnable(() -> sendMessage("users-out-0", new Event(Event.Type.DELETE, userId, null)))
                .subscribeOn(publishEventScheduler).then();
    }

    private void sendMessage(String bindingName, Event event) {
        LOG.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        Message message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }

    public Mono<Health> getProductHealth() {
        return getHealth(productServiceUrl);
    }

    public Mono<Health> getOrderHealth() {
        return getHealth(orderServiceUrl);
    }

    public Mono<Health> getUserHealth() {
        return getHealth(userServiceUrl);
    }

    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        LOG.debug("Will call the Health API on URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log(LOG.getName(), FINE);
    }

    private Throwable handleException(Throwable ex) {
        if (!(ex instanceof WebClientResponseException)) {
            LOG.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        WebClientResponseException wcre = (WebClientResponseException) ex;

        LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
        LOG.warn("Error body: {}", wcre.getResponseBodyAsString());
        return ex;
    }
}

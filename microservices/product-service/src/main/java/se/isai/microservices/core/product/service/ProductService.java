package se.isai.microservices.core.product.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.isai.microservices.core.product.dto.Product;

import java.util.List;

public interface ProductService {
    public Flux<Product> getProductList();
    public Mono<Product> saveProduct(Product product);
    public Mono<Product> getProduct(String productId);
    public Mono<Void> deleteProduct(String productId);
}

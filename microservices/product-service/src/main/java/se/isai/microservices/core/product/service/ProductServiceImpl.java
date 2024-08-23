package se.isai.microservices.core.product.service;

import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.isai.microservices.core.product.dto.Product;
import se.isai.microservices.core.product.persistence.ProductEntity;
import se.isai.microservices.core.product.persistence.ProductRepository;

import java.util.logging.Level;

import static java.util.logging.Level.FINE;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public Flux<Product> getProductList() {
        return productRepository.findAll()
                .switchIfEmpty(
                        Flux.error(new Exception("Products Not Found"))
                )
                .log(LOG.getName(), FINE)
                .map( productEntity -> {
                    return new Product(
                            productEntity.getId(),
                            productEntity.getProductName(),
                            productEntity.getProductPrice(),
                            productEntity.getProductDescription(),
                            productEntity.getProductCalories(),
                            productEntity.getImageUrl()
                    );
                });
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        LOG.info("product being saved: " + product.getProductName());
        ProductEntity productEntity = new ProductEntity(
                product.getProductName(),
                product.getProductPrice(),
                product.getProductDescription(),
                product.getProductCalories(),
                product.getImageUrl()
        );

        Mono<Product> newEntity = productRepository.save(productEntity)
                .log(LOG.getName(), FINE)
                .onErrorMap(DuplicateKeyException.class, e -> new Exception("Duplicate key, Product Id: " + product.getProductId()))
                .map(createdProduct -> {
                    return new Product(
                            createdProduct.getId(),
                            createdProduct.getProductName(),
                            createdProduct.getProductPrice(),
                            createdProduct.getProductDescription(),
                            createdProduct.getProductCalories(),
                            createdProduct.getImageUrl()
                    );
                });

        return newEntity;
    }

    @Override
    public Mono<Product> getProduct(String productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(
                        Mono.error(new Exception("Products Not Found"))
                )
                .log(LOG.getName(), FINE)
                .map( productEntity -> {
                    return new Product(
                            productEntity.getId(),
                            productEntity.getProductName(),
                            productEntity.getProductPrice(),
                            productEntity.getProductDescription(),
                            productEntity.getProductCalories(),
                            productEntity.getImageUrl()
                    );
                });
    }

    @Override
    public Mono<Void> deleteProduct(String productId) {

        return productRepository.findById(productId).log(LOG.getName(), FINE).map(e -> productRepository.delete(e)).flatMap(e -> e);
    }
}

package se.isai.microservices.core.product.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.isai.microservices.core.product.dto.Product;
import se.isai.microservices.core.product.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping(
            value = "/{productId}",
            produces = "application/json")
    Mono<Product> getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

    @GetMapping(
            value = "/list",
            produces = "application/json")
    Flux<Product> getProductList() {
        return productService.getProductList();
    }
}

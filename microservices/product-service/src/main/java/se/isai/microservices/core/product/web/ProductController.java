package se.isai.microservices.core.product.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    Product getProduct(@PathVariable String productId) {
        return productService.getProduct(productId);
    }

    @GetMapping(
            value = "/list",
            produces = "application/json")
    List<Product> getProductList() {
        return productService.getProductList();
    }

    @PostMapping("/saveItem")
    public Product saveProductItem(@RequestBody Product product){
        return productService.saveProduct(product);
    }
}

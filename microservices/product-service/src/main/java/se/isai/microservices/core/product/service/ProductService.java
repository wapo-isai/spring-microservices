package se.isai.microservices.core.product.service;

import se.isai.microservices.core.product.dto.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getProductList();
    public Product saveProduct(Product product);
    public Product getProduct(String productId);
    public void deleteProduct(String productId);
}

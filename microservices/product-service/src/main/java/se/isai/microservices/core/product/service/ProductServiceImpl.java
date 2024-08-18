package se.isai.microservices.core.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.isai.microservices.core.product.dto.Product;
import se.isai.microservices.core.product.persistence.ProductEntity;
import se.isai.microservices.core.product.persistence.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getProductList() {
        List<ProductEntity> productEntities = (List<ProductEntity>) productRepository.findAll();
        List<Product> products = new ArrayList<>();

        for(ProductEntity productEntity : productEntities) {
            Product product = new Product(
                    productEntity.getId(),
                    productEntity.getProductName(),
                    productEntity.getProductPrice(),
                    productEntity.getProductDescription(),
                    productEntity.getProductCalories(),
                    productEntity.getImageUrl()
            );

            products.add(product);
        }

        return products;
    }

    @Override
    public Product saveProduct(Product product) {
        ProductEntity productEntity = new ProductEntity(
                product.getProductName(),
                product.getProductPrice(),
                product.getProductDescription(),
                product.getProductCalories(),
                product.getImageUrl()
        );

        productEntity = productRepository.save(productEntity);

        Product newProduct = new Product(
                productEntity.getId(),
                productEntity.getProductName(),
                productEntity.getProductPrice(),
                productEntity.getProductDescription(),
                productEntity.getProductCalories(),
                productEntity.getImageUrl()
        );

        return newProduct;
    }

    @Override
    public Product getProduct(String productId) {
        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        Product newProduct = new Product();

        if(productEntity.isPresent()) {
            newProduct = new Product(
                    productEntity.get().getId(),
                    productEntity.get().getProductName(),
                    productEntity.get().getProductPrice(),
                    productEntity.get().getProductDescription(),
                    productEntity.get().getProductCalories(),
                    productEntity.get().getImageUrl()
            );
        }

        return newProduct;
    }

    @Override
    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }
}

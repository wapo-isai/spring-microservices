package se.isai.microservices.core.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import se.isai.microservices.core.product.persistence.ProductEntity;
import se.isai.microservices.core.product.persistence.ProductRepository;

@SpringBootApplication
@EnableMongoRepositories
public class ProductServiceApplication implements CommandLineRunner {
	ProductRepository productRepository;

	@Autowired
	public ProductServiceApplication(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		productRepository.save(new ProductEntity("Sunrise Roast",8.00, "A light roast coffee with citrusy and floral notes.", 170, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Velvet Mocha",10.00, "A rich, creamy blend of espresso and dark chocolate.", 220, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Honey Almond Brew",10.00, "Coffee with a touch of honey and almond milk.", 180, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Toffee Nut Latte",8.00, "Espresso with steamed milk, toffee, and nutty flavors.", 250, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Midnight Espresso",12.00, "A classic, strong shot of espresso, perfect for late nights.", 150, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Caramel Cloud Macchiato",9.00, "A frothy macchiato with a swirl of caramel.", 200, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Maple Pecan Cappuccino",8.00, "A cappuccino with maple syrup and pecan flavor.", 230, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Coconut Cream Cold Bre",13.00, "Cold brew coffee with coconut cream and a hint of vanilla.", 190, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Saffron Spiced Coffee",11.00, "Coffee with saffron, cardamom, and a dash of cinnamon.", 150, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
		productRepository.save(new ProductEntity("Hazelnut Chai Fusion",9.00, "A unique mix of chai spices and hazelnut-flavored coffee.", 240, "https://brewed-awakening-coffee-shop.s3.amazonaws.com/sunrise-roast.webp"));
	}
}

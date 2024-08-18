package se.isai.microservices.composite.product.dto;

public class Product {
    private String productId;

    private String productName;

    private Double productPrice;

    private String productDescription;

    private int productCalories;

    private String imageUrl;

    public  Product(){}

    public Product(String productId, String productName, Double productPrice, String productDescription, int productCalories, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productCalories = productCalories;
        this.imageUrl = imageUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductCalories() {
        return productCalories;
    }

    public void setProductCalories(int productCalories) {
        this.productCalories = productCalories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

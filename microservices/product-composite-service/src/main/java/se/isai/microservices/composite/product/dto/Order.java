package se.isai.microservices.composite.product.dto;

import java.util.List;

public class Order {
    private String userId;

    private String orderNumber;

    private float totalPrice;

    private List<String> productIds;

    public Order(String userId, String orderNumber, float totalPrice, List<String> productIds) {
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.productIds = productIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}


package com.mateusz113.shop.model;

import java.math.BigDecimal;

public class Product {
    private final String id;
    private final String name;
    private BigDecimal price;
    private int quantity;

    public Product(String id, String name, BigDecimal price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

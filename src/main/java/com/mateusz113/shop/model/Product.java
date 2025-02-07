package com.mateusz113.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Class that holds the product information.
 */
public class Product implements Serializable {
    private final String id;
    private final String name;
    private BigDecimal price;
    private int quantity;

    /**
     * Constructor of the {@code Product} class.
     *
     * @param id ID of the product.
     * @param name name of the product.
     * @param price price of the product.
     * @param quantity quantity of the product.
     */
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("""
                Nazwa: %s
                Cena: %.2f zł
                Ilość: %d
                """, name, price, quantity);
    }
}

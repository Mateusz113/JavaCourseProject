package com.mateusz113.shop.util;

import com.mateusz113.shop.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Class holding the utility functions for the product.
 */
public class ProductUtil {
    /**
     * Retrieve the total cost of a product list.
     *
     * @param products list of products that price will be calculated of.
     * @return {@code BigDecimal} representing the total list price.
     */
    public static BigDecimal getTotalProductsPrice(List<Product> products) {
        return products
                .stream()
                .map(product -> BigDecimal.valueOf(product.getPrice().doubleValue() * product.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

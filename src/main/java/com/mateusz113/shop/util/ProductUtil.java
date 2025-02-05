package com.mateusz113.shop.util;

import com.mateusz113.shop.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class ProductUtil {
    public static BigDecimal getTotalProductsPrice(List<Product> products) {
        return products
                .stream()
                .map(product -> BigDecimal.valueOf(product.getPrice().doubleValue() * product.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.mateusz113.shop.manager;

import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderManager implements Serializable {
    private final List<Order> orders = new ArrayList<>();

    public List<Order> getUserOrders(String userId) {
        return orders.stream().filter(order -> order.userId().equals(userId)).toList();
    }

    public void addNewOrder(String userId, List<Product> products) {
        orders.add(new Order(userId, LocalDateTime.now(), products));
    }
}

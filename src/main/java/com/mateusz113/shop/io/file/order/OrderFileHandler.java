package com.mateusz113.shop.io.file.order;

public abstract class OrderFileHandler {
    private String orderDetailsFolder;

    public OrderFileHandler() {
        this.orderDetailsFolder = "";
    }

    public String getOrderDetailsFolder() {
        return orderDetailsFolder;
    }

    public void setOrderDetailsFolder(String orderDetailsFolder) {
        this.orderDetailsFolder = orderDetailsFolder;
    }
}

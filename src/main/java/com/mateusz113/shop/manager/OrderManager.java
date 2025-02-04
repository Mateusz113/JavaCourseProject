package com.mateusz113.shop.manager;

import com.mateusz113.shop.converter.ProductConverter;
import com.mateusz113.shop.exception.IllegalFormatException;
import com.mateusz113.shop.io.file.order.OrderFileReader;
import com.mateusz113.shop.io.file.order.OrderFileWriter;
import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<Order> previousOrders;
    private final List<Order> newOrders;

    public OrderManager() {
        this.previousOrders = new ArrayList<>();
        this.newOrders = new ArrayList<>();
    }

    public List<Order> getAllOrders() {
        List<Order> allOrders = new ArrayList<>();
        allOrders.addAll(previousOrders);
        allOrders.addAll(newOrders);
        return allOrders;
    }

    public void addNewOrder(String userId, List<Product> products) {
        newOrders.add(new Order(userId, LocalDateTime.now(), products));
    }

    public void readPreviousOrdersFromFiles(String userId, OrderFileReader orderFileReader, ProductConverter productConverter) throws IOException, IllegalFormatException {
        try {
            List<List<String>> ordersData = orderFileReader.readOldOrders(userId);
            for (List<String> orderData : ordersData) {
                addPreviousOrderFromData(orderData, productConverter);
            }
        } catch (IOException e) {
            throw new IOException("Nie można wczytać wcześniejszych zamówień");
        }
    }

    public void writeNewOrdersToFiles(OrderFileWriter orderFileWriter, ProductConverter productConverter) throws IOException {
        List<String> data = new ArrayList<>();
        for (Order order : newOrders) {
            data.add(order.userId());
            String formattedDate = order.placementTime().format(DATE_TIME_FORMAT);
            data.add(formattedDate);
            data.addAll(productConverter.constructDetailsListFromProducts(order.products()));
            String fileName = String.format("order%d-%d-%d_%d-%d-%d.txt",
                    order.placementTime().getDayOfMonth(),
                    order.placementTime().getMonthValue(),
                    order.placementTime().getYear(),
                    order.placementTime().getHour(),
                    order.placementTime().getMinute(),
                    order.placementTime().getSecond()
            );
            try {
                orderFileWriter.writeOrderToFile(data, String.format("%s/%s", orderFileWriter.getOrderDetailsFolder(), fileName));
            } catch (IOException e) {
                throw new IOException("Nie można zapisać zamówień");
            }
        }
    }

    private void addPreviousOrderFromData(List<String> data, ProductConverter productConverter) throws IllegalFormatException {
        if (data.size() < 3) {
            throw new IllegalFormatException("Informacie o zamówieniu są w złym formacie!");
        }
        String userId = data.getFirst();
        LocalDateTime placementTime = LocalDateTime.parse(data.get(1), DATE_TIME_FORMAT);
        List<Product> products = new ArrayList<>();
        for (int i = 2; i < data.size(); i++) {
            products.add(productConverter.constructProductFromDetails(data.get(i)));
        }
        previousOrders.add(new Order(userId, placementTime, products));
    }
}

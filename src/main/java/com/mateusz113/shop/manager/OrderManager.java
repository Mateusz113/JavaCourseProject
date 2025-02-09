package com.mateusz113.shop.manager;

import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mateusz113.shop.util.ProductUtil.getTotalProductsPrice;

/**
 * Class that holds and manages app order data.
 */
public class OrderManager implements Serializable {
    private final List<Order> orders = new ArrayList<>();

    /**
     * Retrieves orders of the user.
     *
     * @param userId ID of the user to retrieve orders of.
     * @return {@code List<Order>} of the user.
     */
    public List<Order> getUserOrders(String userId) {
        return orders.stream().filter(order -> order.userId().equals(userId)).toList();
    }

    public void addNewOrder(Order order) {
        orders.add(order);
    }

    /**
     * Outputs the invoice for the order into the text file on Desktop.
     *
     * @param order order for the invoice.
     * @param user user that placed the order.
     * @throws IOException if a file cannot be outputted.
     */
    public void outputInvoice(Order order, User user) throws IOException {
        String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
        LocalDateTime orderPlacementTime = order.placementTime();
        String fileName = String.format("Faktura z dnia %d.%d - godz. %d_%d.txt",
                orderPlacementTime.getDayOfMonth(),
                orderPlacementTime.getMonthValue(),
                orderPlacementTime.getHour(),
                orderPlacementTime.getMinute()
        );
        String filePath = desktopPath + File.separator + fileName;
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))
        ) {
            writer.write("****** FAKTURA ZE SKLEPU \"ELECTRONICS SHOP\" ******\n");
            writer.write("Wystawiający: Electronics Shop\n");
            writer.write(String.format("Kupujący: %s %s%n", user.firstName(), user.lastName()));
            writer.write(String.format("Email kupującego: %s%n", user.email()));
            writer.write("Zamówione produkty:\n");
            for (Product p : order.products()) {
                writer.write(p.toString());
                writer.newLine();
            }
            writer.write(String.format("Całkowita kwota zamówienia wyniosła: %.2f zł%n", getTotalProductsPrice(order.products())));
            writer.write("Podpisano: Electronics Shop");
        }
    }
}

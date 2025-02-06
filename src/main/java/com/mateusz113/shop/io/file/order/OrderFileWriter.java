package com.mateusz113.shop.io.file.order;

import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.mateusz113.shop.util.ProductUtil.getTotalProductsPrice;

public class OrderFileWriter extends OrderFileHandler {
    private boolean append;

    public OrderFileWriter() {
        append = false;
    }

    public void shouldAppend(boolean append) {
        this.append = append;
    }

    public void createInvoice(Order order, User user) throws IOException {
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
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))
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

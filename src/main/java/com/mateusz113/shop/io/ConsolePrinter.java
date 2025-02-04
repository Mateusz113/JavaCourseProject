package com.mateusz113.shop.io;

import com.mateusz113.shop.model.Cart;
import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsolePrinter {
    public static void printLine(String line) {
        System.out.println(line);
    }

    public static void printGreeting() {
        printLine("***** WITAMY W APLIKACJI ELECTRONICS SHOP *****");
    }

    public static void printProductsManagementMenu() {
        printLine("Co chcesz zrobić?");
        printLine("0. Wróć do głównego menu");
        printLine("1. Dodaj produkt");
        printLine("2. Zmień ilość produktu");
    }

    public static void printProducts(List<Product> products) {
        int i = 1;
        for (Product p : products) {
            if (p.getQuantity() > 0) {
                printLine(i + ".");
                printLine(p.toString());
                i++;
            }
        }
    }

    public static void printPreviousOrders(List<Order> orders) {
        printLine("Twoje poprzednie zamówienia:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for (Order order : orders) {
            String formattedDate = order.placementTime().format(formatter);
            printLine("Data: " + formattedDate);
            printProducts(order.products());
        }
    }

    public static <T> void printMenuOptions(T[] options) {
        for (T option : options) {
            printLine(option.toString());
        }
    }

    public static void printCart(Cart cart) {
        printLine("Wózek:\n");
        printProducts(cart.getProducts());
        BigDecimal sumOfPrices = cart
                .getProducts()
                .stream()
                .map(product -> BigDecimal.valueOf(product.getPrice().doubleValue() * product.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        printLine(String.format("Całkowita wartość twojego koszyka wynosi: %.2f zł", sumOfPrices));
    }
}

package com.mateusz113.shop.io.console;

import com.mateusz113.shop.manager.CartManager;
import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.mateusz113.shop.util.ProductUtil.getTotalProductsPrice;

/**
 * Console printing class.
 */
public class ConsolePrinter {
    public static void printLine(String line) {
        System.out.println(line);
    }

    /**
     * Prints greeting to the application.
     */
    public static void printGreeting() {
        printLine("***** WITAMY W APLIKACJI ELECTRONICS SHOP *****");
    }

    public static void printProductsManagementMenu() {
        printLine("Co chcesz zrobić?");
        printLine("0. Wróć do głównego menu");
        printLine("1. Dodaj produkt");
        printLine("2. Zmień ilość produktu");
    }

    /**
     * Prints products info to the console.
     *
     * @param products list of products to be printed.
     */
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

    /**
     * Prints orders info to the console.
     *
     * @param orders list of orders to be printed.
     */
    public static void printPreviousOrders(List<Order> orders) {
        printLine("Twoje poprzednie zamówienia:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        int i = 1;
        for (Order order : orders) {
            String formattedDate = order.placementTime().format(formatter);
            printLine(String.format("%d) Data: %s", i, formattedDate));
            printProducts(order.products());
            i++;
        }
        printLine("Wybierz numer zamównienia z którego chcesz wystawić fakturę, lub wpisz 0 by wrócić do głównego menu.");
    }

    /**
     * Prints menu options info to the console.
     *
     * @param options list of options to be printed.
     */
    public static <T> void printMenuOptions(T[] options) {
        for (T option : options) {
            printLine(option.toString());
        }
    }

    /**
     * Prints cart info to the console.
     *
     * @param cartManager cart manager of the application.
     */
    public static void printCart(CartManager cartManager) {
        printLine("Wózek:\n");
        printProducts(cartManager.getProducts());
        BigDecimal sumOfPrices = getTotalProductsPrice(cartManager.getProducts());
        printLine(String.format("Całkowita wartość twojego koszyka wynosi: %.2f zł", sumOfPrices));
    }
}

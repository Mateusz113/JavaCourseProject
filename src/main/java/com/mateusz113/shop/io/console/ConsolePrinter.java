package com.mateusz113.shop.io.console;

import com.mateusz113.shop.manager.CartManager;
import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.util.ProductUtil;

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
    public static void printError(String line) {
        System.err.println(line);
    }

    /**
     * Prints greeting to the application.
     */
    public static void printGreeting() {
        printLine("***** WITAMY W APLIKACJI ELECTRONICS SHOP *****");
    }

    /**
     * Prints register confirmation to the user.
     */
    public static void printRegisterConfirmation() {
        printLine("Pomyślnie zarejestrowano.\n");
    }

    /**
     * Prints login confirmation to the user.
     */
    public static void printLoginConfirmation(){
        printLine("Pomyślnie zalogowano.\n");
    }

    /**
     * Prints user-specific greeting.
     *
     * @param userFirstName first name of the logged user.
     */
    public static void printUserGreeting(String userFirstName) {
        printLine(String.format("Witaj %s!", userFirstName));
    }

    /**
     * Prints goodbye to the user.
     */
    public static void printGoodbye() {
        printLine("***** ZAPRASZAMY PONOWNIE! *****");
    }

    /**
     * Prints menu options info to the console.
     *
     * @param options list of options to be printed.
     */
    public static <T> void printMenuOptions(T[] options) {
        printLine("**************************");
        for (T option : options) {
            printLine(option.toString());
        }
        printLine("**************************");
    }

    /**
     * Print shop UI and available products.
     * Returns prematurely if the available products are empty.
     *
     * @param products list of shop-available products.
     */
    public static void printShop(List<Product> products) {
        if (products.isEmpty()) {
            printError("Brak dostępnych produktów w sprzedaży!");
            return;
        }
        printLine("Produkty dostępne w ofercie:");
        printProducts(products);
        printLine("Podaj numer produktu który chcesz kupić, lub wpisz 0 by cofnąć do głównego menu.");
    }

    public static void printProductsManagementMenu() {
        printLine("Co chcesz zrobić?");
        printLine("0. Wróć do głównego menu");
        printLine("1. Dodaj produkt");
        printLine("2. Zmień ilość produktu");
    }

    /**
     * Print product quantity management UI and available products.
     * Returns prematurely if the available products are empty.
     *
     * @param products list of shop-available products.
     */
    public static void printQuantitiesManagement(List<Product> products) {
        if (products.isEmpty()) {
            printError("Brak dostępnych produktów w sprzedaży!");
            return;
        }
        printLine("Produkty dostępne do zmiany ilości:");
        printProducts(products);
        printLine("Podaj numer produktu który chcesz zmienić.");
    }

    /**
     * Prints information about the product quantity and asking for new one.
     *
     * @param product product providing the information.
     */
    public static void printProductQuantityInfo(Product product) {
        printLine(String.format("Wprowadź nową ilość: %s. (Poprzednio wynosiła %d)", product.getName(), product.getQuantity()));
    }

    /**
     * Prints cart info to the console.
     * Returns prematurely if products list in cartManager is empty.
     *
     * @param cartManager cart manager of the application.
     */
    public static void printCart(CartManager cartManager) {
        if (cartManager.getProducts().isEmpty()) {
            printError("Wózek jest pusty!");
            return;
        }
        printLine("Wózek:");
        printProducts(cartManager.getProducts());
        BigDecimal sumOfPrices = getTotalProductsPrice(cartManager.getProducts());
        printLine(String.format("Całkowita wartość twojego koszyka wynosi: %.2f zł", sumOfPrices));
        printLine("""
                Co chcesz zrobić?
                0. Wróć do głównego menu.
                1. Opłać zamówienie.
                """);
    }

    /**
     * Prints info about the cart products being impossible to buy.
     */
    public static void printImpossibleCartInfo() {
        printError("Produkty w koszyku posiadały wartości niemożliwe do kupienia! Twój koszyk został zaktualizowany.");
    }

    /**
     * Prints info about the placed order and thank the user.
     *
     * @param order order that was placed.
     */
    public static void printPlacedOrderInfoAndGratitude(Order order) {
        printLine(String.format(
                "Zamówienie zostało złożone o godzinie %d:%d. Zawiera %d produktów o łącznej kwocie %.2f zł.",
                order.placementTime().getHour(),
                order.placementTime().getMinute(),
                order.products().stream().mapToInt(Product::getQuantity).sum(),
                ProductUtil.getTotalProductsPrice(order.products())
                )
        );
        printLine("Dziękujemy!");
    }

    /**
     * Prints orders info to the console.
     * Returns prematurely if the provided order list is empty.
     *
     * @param orders list of orders to be printed.
     */
    public static void printPreviousOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            printError("Nie masz posiadasz złożonych zamówień!");
            return;
        }
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
     * Prints the info about created invoice to the console.
     *
     * @param invoiceOrder order for which the invoice was created.
     */
    public static void printInvoiceCreationInfo(Order invoiceOrder){
        printLine(String.format(
                "Twoja faktura z dnia: %d.%d została zapisana na pulpicie.",
                invoiceOrder.placementTime().getDayOfMonth(),
                invoiceOrder.placementTime().getMonthValue()
        ));
    }

    /**
     * Prints products info to the console.
     *
     * @param products list of products to be printed.
     */
    private static void printProducts(List<Product> products) {
        int i = 1;
        for (Product p : products) {
            if (p.getQuantity() > 0) {
                printLine(i + ".");
                printLine(p.toString());
                i++;
            }
        }
    }
}

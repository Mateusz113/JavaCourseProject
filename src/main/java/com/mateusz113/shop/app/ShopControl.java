package com.mateusz113.shop.app;

import com.mateusz113.shop.app.menu_navigation.AuthOption;
import com.mateusz113.shop.app.menu_navigation.MainOption;
import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.exception.NoSuchOptionException;
import com.mateusz113.shop.exception.NoSuchUserException;
import com.mateusz113.shop.exception.UserAlreadyExistsException;
import com.mateusz113.shop.io.console.ConsoleReader;
import com.mateusz113.shop.manager.CartManager;
import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mateusz113.shop.io.console.ConsolePrinter.*;

public class ShopControl {
    private final ManagersDataHandler managersDataHandler;
    private final ConsoleReader consoleReader;
    private final CartManager cartManager;
    private User currentUser;

    public ShopControl() {
        managersDataHandler = new ManagersDataHandler();
        consoleReader = new ConsoleReader();
        cartManager = new CartManager();
    }

    public void start() {
        managersDataHandler.loadManagers();
        printGreeting();
        while (currentUser == null) {
            authLoop();
        }
        mainLoop();
    }

    private void authLoop() {
        AuthOption option;
        printMenuOptions(AuthOption.values());
        option = getAuthOption();
        switch (option) {
            case EXIT -> exit();
            case LOGIN -> login();
            case REGISTER -> register();
        }
    }

    private void login() {
        LoginDetails details = consoleReader.readLoginDetails();
        try {
            currentUser = managersDataHandler.getAuthManager().loginUser(details);
            printLine("Pomyślnie zalogowano.\n");
        } catch (NoSuchUserException e) {
            printLine(e.getMessage());
        }
    }

    private void register() {
        RegisterDetails details = consoleReader.readRegisterDetails();
        try {
            currentUser = managersDataHandler.getAuthManager().registerUser(details);
            printLine("Pomyślnie zarejestrowano.\n");
        } catch (UserAlreadyExistsException e) {
            printLine(e.getMessage());
        }
    }

    private void mainLoop() {
        printLine(String.format("Witaj %s!", currentUser.firstName()));
        MainOption option;
        do {
            printMenuOptions(MainOption.values());
            option = getMainOption();
            switch (option) {
                case EXIT -> exit();
                case SHOP -> shop();
                case MANAGE_PRODUCTS -> manageProducts();
                case CART -> cart();
                case PREVIOUS_ORDERS -> previousOrders();
            }
        } while (option != MainOption.EXIT);
    }

    private void exit() {
        managersDataHandler.saveManagers();
        printLine("Zapraszamy ponownie!");
    }

    private void shop() {
        List<Product> shopProducts = managersDataHandler.getShopManager().getProducts();
        if (shopProducts.isEmpty()) {
            printLine("Brak dostępnych produktów w sklepie.");
            return;
        }
        printProducts(shopProducts);
        printLine("Podaj numer produktu który chcesz kupić, lub wpisz 0 by cofnąć do głównego menu.");
        int input = consoleReader.readIntValue(0, shopProducts.size());
        if (input == 0) return;
        Product chosenProduct = shopProducts.get(input - 1);
        Product configuredProduct = consoleReader.configureProduct(chosenProduct);
        cartManager.addProduct(configuredProduct);
    }

    private void manageProducts() {
        printProductsManagementMenu();
        int input = consoleReader.readIntValue(0, 2);
        switch (input) {
            case 1 -> managersDataHandler.getShopManager().addProduct(consoleReader.readProductFromKeyboard());
            case 2 -> manageProductQuantities();
        }
        managersDataHandler.saveShopManager();
    }

    private void manageProductQuantities() {
        List<Product> shopProducts = managersDataHandler.getShopManager().getProducts();
        if (shopProducts.isEmpty()) {
            printLine("Brak dostępnych produktów w sklepie.");
            return;
        }
        printProducts(shopProducts);
        printLine("Podaj numer produktu którego ilość chcesz zmienić:");
        int input = consoleReader.readIntValue(1, shopProducts.size());
        Product chosenProduct = shopProducts.get(input - 1);
        printLine(String.format("Wprowadź nową ilość produktu (poprzednio wynosiła %d)", chosenProduct.getQuantity()));
        input = consoleReader.readIntValue(0, chosenProduct.getQuantity());
        managersDataHandler.getShopManager().updateProductQuantity(chosenProduct.getId(), input);
    }

    private void cart() {
        if (cartManager.getProducts().isEmpty()) {
            printLine("Twój koszyk jest pusty!");
            return;
        }
        printCart(cartManager);
        printLine("""
                Co chcesz zrobić?
                0. Wróć do głównego menu.
                1. Opłać zamówienie.
                """);
        int input = consoleReader.readIntValue(0, 1);
        if (input == 0) return;
        placeOrder();
    }

    private void placeOrder() {
        //Reload shop data before checking for availability to make sure it is up-to-date
        managersDataHandler.reloadShopManager();
        if (!cartManager.areCartProductsAvailable(managersDataHandler.getShopManager().getProducts())) {
            cartManager.updateCartProductsQuantities(managersDataHandler.getShopManager().getProducts());
            printLine("Produkty w koszyku posiadały wartości niemożliwe do kupienia! Twój koszyk został zaktualizowany.");
            return;
        }
        List<Product> orderedProducts = new ArrayList<>(cartManager.getProducts());
        managersDataHandler.getOrderManager().addNewOrder(currentUser.id(), orderedProducts);
        managersDataHandler.getShopManager().updateSoldProductsQuantity(cartManager.getProducts());
        cartManager.clearProducts();
        //Save data immediately after checkout to make sure other users will use the updated version
        managersDataHandler.saveShopManager();
        printLine("Dziękujemy za złożenie zamówienia!");
    }

    private void previousOrders() {
        List<Order> orders = managersDataHandler.getOrderManager().getUserOrders(currentUser.id());
        if (orders.isEmpty()) {
            printLine("Nie masz poprzednich zamówień!");
            return;
        }
        printPreviousOrders(orders);
        int input = consoleReader.readIntValue(0, orders.size());
        if (input != 0) {
            Order chosenOrder = orders.get(input - 1);
            try {
                managersDataHandler.getOrderManager().outputInvoice(chosenOrder, currentUser);
            } catch (IOException e) {
                printLine(e.getMessage());
            } finally {
                printLine(String.format(
                        "Twoja faktura z dnia: %d.%d została zapisana na pulpicie.",
                        chosenOrder.placementTime().getDayOfMonth(),
                        chosenOrder.placementTime().getMonthValue()
                ));
            }
        }
    }

    private AuthOption getAuthOption() {
        boolean optionIsValid = false;
        AuthOption option = null;
        while (!optionIsValid) {
            try {
                int value = consoleReader.readIntValue();
                option = AuthOption.getOptionFromValue(value);
                optionIsValid = true;
            } catch (NoSuchOptionException e) {
                printLine(e.getMessage());
            }
        }
        return option;
    }

    private MainOption getMainOption() {
        boolean optionIsValid = false;
        MainOption option = null;
        while (!optionIsValid) {
            try {
                int value = consoleReader.readIntValue();
                option = MainOption.getOptionFromValue(value);
                optionIsValid = true;
            } catch (NoSuchOptionException e) {
                printLine(e.getMessage());
            }
        }
        return option;
    }
}

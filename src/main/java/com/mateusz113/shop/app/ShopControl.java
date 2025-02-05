package com.mateusz113.shop.app;

import com.mateusz113.shop.app.menu_navigation.AuthOption;
import com.mateusz113.shop.app.menu_navigation.MainOption;
import com.mateusz113.shop.auth.AuthManager;
import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.converter.ProductConverter;
import com.mateusz113.shop.exception.IllegalFormatException;
import com.mateusz113.shop.exception.NoSuchOptionException;
import com.mateusz113.shop.exception.NoSuchUserException;
import com.mateusz113.shop.exception.UserAlreadyExistsException;
import com.mateusz113.shop.io.ConsoleReader;
import com.mateusz113.shop.io.file.order.OrderFileReader;
import com.mateusz113.shop.io.file.order.OrderFileWriter;
import com.mateusz113.shop.io.file.product.ProductFileReader;
import com.mateusz113.shop.io.file.product.ProductFileWriter;
import com.mateusz113.shop.io.file.user.UserFileReader;
import com.mateusz113.shop.io.file.user.UserFileWriter;
import com.mateusz113.shop.manager.OrderManager;
import com.mateusz113.shop.model.Cart;
import com.mateusz113.shop.model.Order;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.mateusz113.shop.io.ConsolePrinter.*;

public class ShopControl {
    private final String USER_LOGIN_INFO_FILE_PATH = "src/main/resources/data/userLoginInfo.txt";
    private final String USER_DETAILS_FILE_PATH = "src/main/resources/data/userData.txt";
    private final String PRODUCT_DETAILS_FILE_PATH = "src/main/resources/data/productData.txt";
    private final AuthManager authManager;
    private final OrderManager orderManager;
    private final ProductConverter productConverter;
    private final ConsoleReader consoleReader;
    private final UserFileReader userFileReader;
    private final UserFileWriter userFileWriter;
    private final ProductFileReader productFileReader;
    private final ProductFileWriter productFileWriter;
    private final OrderFileReader orderFileReader;
    private final OrderFileWriter orderFileWriter;
    private final Cart cart;
    private User currentUser;
    private List<Product> shopProducts;

    public ShopControl() {
        this.authManager = new AuthManager();
        this.orderManager = new OrderManager();
        this.productConverter = new ProductConverter();
        this.consoleReader = new ConsoleReader();
        this.userFileReader = new UserFileReader(USER_LOGIN_INFO_FILE_PATH, USER_DETAILS_FILE_PATH);
        this.userFileWriter = new UserFileWriter(USER_LOGIN_INFO_FILE_PATH, USER_DETAILS_FILE_PATH);
        this.productFileReader = new ProductFileReader(PRODUCT_DETAILS_FILE_PATH);
        this.productFileWriter = new ProductFileWriter(PRODUCT_DETAILS_FILE_PATH);
        this.orderFileReader = new OrderFileReader();
        this.orderFileWriter = new OrderFileWriter();
        cart = new Cart();
        shopProducts = new ArrayList<>();
    }

    public void start() {
        printGreeting();
        checkForUserFiles();
        loadProducts();
        authLoop();
    }

    private void loadProducts() {
        CompletableFuture.runAsync(() -> {
            try {
                shopProducts = productConverter.constructProductListFromDetails(productFileReader.getProductsFromFile());
                Collections.shuffle(shopProducts);
            } catch (IOException | IllegalFormatException e) {
                printLine("Aplikacja napotkała problem: " + e.getMessage());
            }
        });
    }

    private void checkForUserFiles() {
        try {
            if (!Files.exists(Paths.get(userFileReader.getUserLoginInfoPath()))) {
                userFileWriter.createLoginInfoFile();
            }
            if (!Files.exists(Paths.get(userFileReader.getUserDetailsPath()))) {
                userFileWriter.createUserDetailsFile();
            }
        } catch (IOException e) {
            printLine("Aplikacja napotkała błąd: " + e.getMessage());
        }
    }

    public void authLoop() {
        AuthOption option;
        printMenuOptions(AuthOption.values());
        option = getAuthOption();
        switch (option) {
            case EXIT -> exit();
            case LOGIN -> login();
            case REGISTER -> register();
        }
    }

    public void login() {
        LoginDetails details = consoleReader.readLoginDetails();
        try {
            currentUser = authManager.loginUser(details, userFileReader);
            printLine("Pomyślnie zalogowano.\n");
            mainLoop();
        } catch (NoSuchUserException e) {
            printLine(e.getMessage());
            authLoop();
        } catch (IOException e) {
            printLine(e.getMessage());
            exit();
        }
    }

    public void register() {
        RegisterDetails details = consoleReader.readRegisterDetails();
        try {
            currentUser = authManager.registerUser(details, userFileWriter, userFileReader);
            printLine("Pomyślnie zarejestrowano.\n");
            mainLoop();
        } catch (UserAlreadyExistsException e) {
            printLine(e.getMessage());
            authLoop();
        } catch (IOException e) {
            printLine(e.getMessage());
            exit();
        }
    }

    public void mainLoop() {
        printLine(String.format("Witaj %s!", currentUser.firstName()));
        configureOrderHandlers();
        readPreviousOrders();
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

    private void configureOrderHandlers() {
        String userOrdersPath = String.format("src/main/resources/data/%s", currentUser.id());
        orderFileReader.setOrderDetailsFolder(userOrdersPath);
        orderFileWriter.setOrderDetailsFolder(userOrdersPath);
    }

    private void readPreviousOrders() {
        CompletableFuture.runAsync(() -> {
            try {
                orderManager.readPreviousOrdersFromFiles(currentUser.id(), orderFileReader, productConverter);
            } catch (IOException | IllegalFormatException e) {
                printLine(e.getMessage());
            }
        });
    }

    private void exit() {
        try {
            productFileWriter.saveShopProductData(productConverter.constructDetailsListFromProducts(shopProducts));
            orderManager.writeNewOrdersToFiles(orderFileWriter, productConverter);
        } catch (IOException e) {
            printLine(e.getMessage());
        }
        printLine("Zapraszamy ponownie!");
    }

    private void shop() {
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
        cart.addProduct(configuredProduct);
    }

    private void updateProductQuantity(String id, int newQuantity) {
        Optional<Product> productToUpdate = shopProducts.stream().filter(product -> product.getId().equals(id)).findFirst();
        productToUpdate.ifPresentOrElse(
                product -> {
                    product.setQuantity(newQuantity);
                    if (product.getQuantity() == 0) {
                        shopProducts.remove(product);
                        printLine("Zaktualizowana ilość produktu wynosiła 0, więc został on usunięty.");
                    } else {
                        printLine("Pomyślnie zaktualizowano ilość produktu.");
                    }
                },
                () -> printLine("Nie udało się poprawnie zmienić ilości produktu!")
        );
    }

    private void manageProducts() {
        printProductsManagementMenu();
        int input = consoleReader.readIntValue(0, 2);
        if (input == 0) return;
        switch (input) {
            case 1 -> shopProducts.add(consoleReader.readProductFromKeyboard());
            case 2 -> manageProductQuantities();
        }
    }

    private void manageProductQuantities() {
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
        updateProductQuantity(chosenProduct.getId(), input);
    }

    private void cart() {
        if (cart.getProducts().isEmpty()) {
            printLine("Twój koszyk jest pusty!");
            return;
        }
        printCart(cart);
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
        if (checkForProductsAvailability(cart.getProducts())) {
            printLine("Produkty w koszyku posiadały wartości niemożliwe do kupienia! Twój koszyk został zaktualizowany.");
            return;
        }
        List<Product> orderedProducts = new ArrayList<>(cart.getProducts());
        orderManager.addNewOrder(currentUser.id(), orderedProducts);
        updateShopProductsQuantity(cart.getProducts());
        cart.clearProducts();
        printLine("Dziękujemy za złożenie zamówienia!");
    }

    private void updateShopProductsQuantity(List<Product> products) {
        for (Product product : products) {
            shopProducts
                    .stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst()
                    .ifPresent(p -> p.setQuantity(p.getQuantity() - product.getQuantity()));
        }
        shopProducts = shopProducts.stream().filter(product -> product.getQuantity() > 0).toList();
    }

    private boolean checkForProductsAvailability(List<Product> products) {
        AtomicBoolean productsWereIllegal = new AtomicBoolean(false);
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            Optional<Product> shopProduct = shopProducts.stream().filter(p -> p.getId().equals(product.getId())).findFirst();
            shopProduct.ifPresentOrElse(
                    p -> {
                        if (product.getQuantity() > p.getQuantity()) {
                            product.setQuantity(p.getQuantity());
                            productsWereIllegal.set(true);
                        }
                    },
                    () -> {
                        iterator.remove();
                        productsWereIllegal.set(true);
                    }
            );
        }
        return productsWereIllegal.get();
    }

    private void previousOrders() {
        List<Order> orders = orderManager.getAllOrders();
        printPreviousOrders(orders);
        int input = consoleReader.readIntValue(0, orders.size());
        if (input != 0) {
            Order chosenOrder = orders.get(input - 1);
            try {
                orderFileWriter.createInvoice(chosenOrder, currentUser);
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

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
import com.mateusz113.shop.io.file.product.ProductFileReader;
import com.mateusz113.shop.io.file.product.ProductFileWriter;
import com.mateusz113.shop.io.file.user.UserFileReader;
import com.mateusz113.shop.io.file.user.UserFileWriter;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.User;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.mateusz113.shop.io.ConsolePrinter.*;

public class ShopControl {
    private final String USER_LOGIN_INFO_FILE_PATH = "src/main/resources/data/userLoginInfo.txt";
    private final String USER_DETAILS_FILE_PATH = "src/main/resources/data/userData.txt";
    private final String PRODUCT_DETAILS_FILE_PATH = "src/main/resources/data/productData.txt";
    private final AuthManager authManager;
    private final ProductConverter productConverter;
    private final ConsoleReader consoleReader;
    private final UserFileReader userFileReader;
    private final UserFileWriter userFileWriter;
    private final ProductFileReader productFileReader;
    private final ProductFileWriter productFileWriter;
    private User currentUser;
    private List<Product> shopProducts;

    public ShopControl() {
        this.consoleReader = new ConsoleReader();
        this.userFileReader = new UserFileReader(USER_LOGIN_INFO_FILE_PATH, USER_DETAILS_FILE_PATH);
        this.userFileWriter = new UserFileWriter(USER_LOGIN_INFO_FILE_PATH, USER_DETAILS_FILE_PATH);
        this.productFileReader = new ProductFileReader(PRODUCT_DETAILS_FILE_PATH);
        this.productFileWriter = new ProductFileWriter(PRODUCT_DETAILS_FILE_PATH);
        this.authManager = new AuthManager();
        this.productConverter = new ProductConverter();
    }

    public void start() {
        printGreeting();
        CompletableFuture.runAsync(() -> {
            try {
                shopProducts = productConverter.constructProductListFromDetails(productFileReader.getProductsFromFile());
                Collections.shuffle(shopProducts);
            } catch (IOException | IllegalFormatException e) {
                printLine("Aplikacja napotkała problem: " + e.getMessage());
            }
        });
        authLoop();
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
        try {
            productFileWriter.saveShopProductData(productConverter.constructDetailsListFromProducts(shopProducts));
        } catch (IOException e) {
            printLine(e.getMessage());
        }
        printLine("Zapraszamy ponownie!");
    }

    private void shop() {
        printProducts(shopProducts);
        printLine("Podaj numer produktu który chcesz kupić, lub wpisz 0 by cofnąć do głównego menu.");
        int input = consoleReader.readIntValue(0, shopProducts.size());
        if (input == 0) return;
        Product chosenProduct = shopProducts.get(input - 1);
        consoleReader.configureProduct(chosenProduct);
    }

    private void manageProducts() {
        printProductsManagementMenu();
        int input = consoleReader.readIntValue(0, 2);
        if (input == 0) return;
        switch (input) {
            case 1 -> shopProducts.add(consoleReader.readProductFromKeyboard());
            case 2 -> updateProductQuantity();
        }
    }

    private void updateProductQuantity() {
        printProducts(shopProducts);
        printLine("Podaj numer produktu którego ilość chcesz zmienić:");
        int input = consoleReader.readIntValue(1, shopProducts.size());
        Product chosenProduct = shopProducts.get(input - 1);
        printLine(String.format("Wprowadź nową ilość produktu (poprzednio wynosiła %d)", chosenProduct.getQuantity()));
        input = consoleReader.readIntValue(0, chosenProduct.getQuantity());
        chosenProduct.setQuantity(input);
        if (chosenProduct.getQuantity() == 0) {
            shopProducts.remove(chosenProduct);
        }
        printLine("Pomyślnie zaktualizowano ilość produktu.");
    }

    private void cart() {
        printLine("Wózek");
    }

    private void previousOrders() {
        printLine("Poprzednie zamówienia");
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

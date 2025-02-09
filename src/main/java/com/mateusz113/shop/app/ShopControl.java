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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mateusz113.shop.io.console.ConsolePrinter.*;

/**
 * Class that manages the control of the Shop app.
 */
public class ShopControl {
    private final ManagersDataHandler managersDataHandler;
    private final ConsoleReader consoleReader;
    private final CartManager cartManager;
    private User currentUser;

    /**
     * Constructor of the shop control class.
     */
    public ShopControl() {
        managersDataHandler = new ManagersDataHandler();
        consoleReader = new ConsoleReader();
        cartManager = new CartManager();
    }

    /**
     * Starts the app loop.
     */
    public void start() {
        managersDataHandler.loadManagers();
        printGreeting();
        while (currentUser == null) {
            authLoop();
        }
        mainLoop();
    }

    /**
     * Auth loop of the app.
     */
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

    /**
     * Logins the user into the system.
     */
    private void login() {
        LoginDetails details = consoleReader.readLoginDetails();
        try {
            currentUser = managersDataHandler.getAuthManager().loginUser(details);
            printLoginConfirmation();
        } catch (NoSuchUserException e) {
            printError(e.getMessage());
        }
    }

    /**
     * Registers the user into the system.
     */
    private void register() {
        RegisterDetails details = consoleReader.readRegisterDetails();
        try {
            currentUser = managersDataHandler.getAuthManager().registerUser(details);
            printRegisterConfirmation();
        } catch (UserAlreadyExistsException e) {
            printError(e.getMessage());
        }
    }

    /**
     * Main loop of the app.
     */
    private void mainLoop() {
        printUserGreeting(currentUser.firstName());
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

    /**
     * Exit function of the application.
     */
    private void exit() {
        managersDataHandler.saveManagers();
        printGoodbye();
    }

    /**
     * App shop functionality.
     */
    private void shop() {
        List<Product> shopProducts = managersDataHandler.getShopManager().getProducts();
        printShop(shopProducts);
        if (shopProducts.isEmpty()) return;
        int input = consoleReader.readIntValue(0, shopProducts.size());
        //0 option is return to the main menu.
        if (input == 0) return;
        Product chosenProduct = shopProducts.get(input - 1);
        Product configuredProduct = consoleReader.configureProduct(chosenProduct);
        cartManager.addProduct(configuredProduct);
    }

    /**
     * App managing product functionality.
     */
    private void manageProducts() {
        printProductsManagementMenu();
        int input = consoleReader.readIntValue(0, 2);
        switch (input) {
            case 1 -> managersDataHandler.getShopManager().addProduct(consoleReader.readProductFromKeyboard());
            case 2 -> manageProductQuantities();
        }
        managersDataHandler.saveShopManager();
    }

    /**
     * App product quantities management.
     */
    private void manageProductQuantities() {
        List<Product> shopProducts = managersDataHandler.getShopManager().getProducts();
        printQuantitiesManagement(shopProducts);
        if (shopProducts.isEmpty()) return;
        //Reads the product number to change quantity.
        int input = consoleReader.readIntValue(1, shopProducts.size());
        Product chosenProduct = shopProducts.get(input - 1);
        printProductQuantityInfo(chosenProduct);
        //Read new quantity value.
        input = consoleReader.readIntValue(0, chosenProduct.getQuantity());
        managersDataHandler.getShopManager().updateProductQuantity(chosenProduct.getId(), input);
    }

    /**
     * App cart functionality.
     */
    private void cart() {
        printCart(cartManager);
        if (cartManager.getProducts().isEmpty()) return;
        //0 option is return to the main menu.
        int input = consoleReader.readIntValue(0, 1);
        if (input == 0) return;
        placeOrder();
    }

    /**
     * App order placing functionality.
     */
    private void placeOrder() {
        //Reload shop data before checking for availability to make sure it is up to date
        managersDataHandler.reloadShopManager();
        if (!cartManager.areCartProductsAvailable(managersDataHandler.getShopManager().getProducts())) {
            cartManager.updateCartProductsQuantities(managersDataHandler.getShopManager().getProducts());
            printImpossibleCartInfo();
            return;
        }
        Order newOrder = new Order(currentUser.id(), LocalDateTime.now(), new ArrayList<>(cartManager.getProducts()));
        managersDataHandler.getOrderManager().addNewOrder(newOrder);
        managersDataHandler.getShopManager().updateSoldProductsQuantity(cartManager.getProducts());
        printPlacedOrderInfoAndGratitude(newOrder);
        cartManager.clearProducts();
        //Save data immediately after checkout to make sure other users will use the updated version
        managersDataHandler.saveShopManager();
    }

    /**
     * App previous orders functionality.
     */
    private void previousOrders() {
        List<Order> orders = managersDataHandler.getOrderManager().getUserOrders(currentUser.id());
        printPreviousOrders(orders);
        if (orders.isEmpty()) return;
        int input = consoleReader.readIntValue(0, orders.size());
        if (input != 0) {
            Order chosenOrder = orders.get(input - 1);
            try {
                managersDataHandler.getOrderManager().outputInvoice(chosenOrder, currentUser);
            } catch (IOException e) {
                printLine(e.getMessage());
            } finally {
                printInvoiceCreationInfo(chosenOrder);
            }
        }
    }

    /**
     * Retrieves {@code AuthOption} from user input.
     *
     * @return {@code AuthOption} user chose.
     */
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

    /**
     * Retrieves {@code MainOption} from user input.
     *
     * @return {@code MainOption} user chose.
     */
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

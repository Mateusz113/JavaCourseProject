package com.mateusz113.shop.app;

import com.mateusz113.shop.io.console.ConsolePrinter;
import com.mateusz113.shop.io.file.Serializer;
import com.mateusz113.shop.manager.AuthManager;
import com.mateusz113.shop.manager.OrderManager;
import com.mateusz113.shop.manager.ShopManager;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Data handler for app managers.
 */
public class ManagersDataHandler {
    private static final String AUTH_MANAGER_SERIALIZE_PATH = Paths
            .get("src", "main", "resources", "data", "AuthManager.ser").toString();
    private static final String ORDER_MANAGER_SERIALIZE_PATH = Paths
            .get("src", "main", "resources", "data", "OrderManager.ser").toString();
    private static final String SHOP_MANAGER_SERIALIZE_PATH = Paths
            .get("src", "main", "resources", "data", "ShopManager.ser").toString();

    private AuthManager authManager;
    private OrderManager orderManager;
    private ShopManager shopManager;

    /**
     * Constructor for data handler. Creates an empty folder for managers serialize files, if not present.
     */
    public ManagersDataHandler() {
        try {
            Serializer.createSerializeFolder();
        } catch (IOException e) {
            ConsolePrinter.printLine("Nie udało się stworzyć folderu do serializacji!");
        }
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    /**
     * Asynchronously loads managers from specified serialize files.
     */
    public void loadManagers() {
        CompletableFuture.runAsync(() -> {
            authManager = loadManager(AuthManager::new, AUTH_MANAGER_SERIALIZE_PATH);
            orderManager = loadManager(OrderManager::new, ORDER_MANAGER_SERIALIZE_PATH);
            shopManager = loadManager(ShopManager::new, SHOP_MANAGER_SERIALIZE_PATH);
        });
    }

    /**
     * Reloads shop manager from serialize file.
     */
    public void reloadShopManager() {
        shopManager = loadManager(ShopManager::new, SHOP_MANAGER_SERIALIZE_PATH);
    }


    /**
     * Asynchronously saves all the managers to the serialize files.
     * Makes sure that application does not close before operation is complete.
     */
    public void saveManagers() {
        CompletableFuture<Void> saveAuthManager = CompletableFuture.runAsync(() -> {
            Serializer.exportData(authManager, AUTH_MANAGER_SERIALIZE_PATH);
        });
        CompletableFuture<Void> saveOrderManager = CompletableFuture.runAsync(() -> {
            Serializer.exportData(orderManager, ORDER_MANAGER_SERIALIZE_PATH);
        });
        CompletableFuture<Void> saveShopManager = CompletableFuture.runAsync(() -> {
            Serializer.exportData(shopManager, SHOP_MANAGER_SERIALIZE_PATH);
        });
        CompletableFuture.allOf(saveAuthManager, saveOrderManager, saveShopManager).join();
    }

    /**
     * Saves shop manager to the serialize file.
     */
    public void saveShopManager() {
        CompletableFuture.runAsync(() -> {
            Serializer.exportData(shopManager, SHOP_MANAGER_SERIALIZE_PATH);
        }).join();
    }

    private <T extends Serializable> T loadManager(Supplier<T> defaultSupplier, String filePath) {
        return (T) Serializer.importData(filePath).orElseGet(defaultSupplier);
    }
}

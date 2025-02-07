package com.mateusz113.shop.manager;

import com.mateusz113.shop.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mateusz113.shop.io.console.ConsolePrinter.printLine;

/**
 * Class that holds and manages app product data.
 */
public class ShopManager implements Serializable {
    private List<Product> products = new ArrayList<>();

    /**
     * Retrieves a list of available products.
     *
     * @return {@code List<Product>} available in the store.
     */
    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProduct(Product product) {
        return products.remove(product);
    }

    /**
     * Updates the quantities of the products that were sold.
     * If the product quantities reach 0, they are removed from the store.
     *
     * @param soldProducts list of sold products.
     */
    public void updateSoldProductsQuantity(List<Product> soldProducts) {
        for (Product product : soldProducts) {
            products
                    .stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst()
                    .ifPresent(p -> p.setQuantity(p.getQuantity() - product.getQuantity()));
        }
        removeSoldOutProducts();
    }

    /**
     * Updates the quantity of the specified product.
     * If the product was not found, then print appropriate information to the console.
     *
     * @param id ID of the product to alter.
     * @param newQuantity new quantity of the product.
     */
    public void updateProductQuantity(String id, int newQuantity) {
        Optional<Product> productToUpdate = products
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        productToUpdate.ifPresentOrElse(
                p -> {
                    p.setQuantity(newQuantity);
                    if (p.getQuantity() == 0) {
                        removeProduct(p);
                        printLine("Zaktualizowana ilość produktu wynosiła 0, więc został on usunięty.");
                    } else {
                        printLine("Pomyślnie zaktualizowano ilość produktu.");
                    }
                },
                () -> printLine("Nie udało się poprawnie zmienić ilości produktu!")
        );
    }

    /**
     * Remove sold products.
     */
    private void removeSoldOutProducts() {
        products = products.stream().filter(product -> product.getQuantity() > 0).collect(Collectors.toList());
    }
}

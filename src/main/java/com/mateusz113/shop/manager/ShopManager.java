package com.mateusz113.shop.manager;

import com.mateusz113.shop.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mateusz113.shop.io.console.ConsolePrinter.printLine;

public class ShopManager implements Serializable {
    private List<Product> products = new ArrayList<>();

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProduct(Product product) {
        return products.remove(product);
    }

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

    private void removeSoldOutProducts() {
        products = products.stream().filter(product -> product.getQuantity() > 0).collect(Collectors.toList());
    }
}

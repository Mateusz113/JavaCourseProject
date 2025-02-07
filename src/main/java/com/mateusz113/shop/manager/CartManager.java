package com.mateusz113.shop.manager;

import com.mateusz113.shop.model.Product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that holds and manages app cart data.
 */
public class CartManager {
    private List<Product> products;

    public CartManager() {
        products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void clearProducts() {
        products.clear();
    }

    /**
     * Function checks if the products in the cart are still available.
     *
     * @param availableProducts products available in the store.
     * @return true if the products can be bought, and false otherwise.
     */
    public boolean areCartProductsAvailable(List<Product> availableProducts) {
        AtomicBoolean productsAreLegal = new AtomicBoolean(true);
        for (Product product : products) {
            Optional<Product> availableProduct = availableProducts
                    .stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst();
            availableProduct.ifPresentOrElse(
                    p -> {
                        if (product.getQuantity() > p.getQuantity()) {
                            productsAreLegal.set(false);
                        }
                    },
                    () -> {
                        productsAreLegal.set(false);
                    }
            );
            if (!productsAreLegal.get()) return false;
        }
        return productsAreLegal.get();
    }

    /**
     * Updates the product quantities in the cart.
     * If the product is no longer available, then it removes it.
     * If the product desired quantity is higher than available, then set it to available max.
     *
     * @param availableProducts products available in the store.
     */
    public void updateCartProductsQuantities(List<Product> availableProducts) {
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            Optional<Product> shopProduct = availableProducts
                    .stream()
                    .filter(p -> p.getId().equals(product.getId()))
                    .findFirst();
            shopProduct.ifPresentOrElse(
                    p -> {
                        if (product.getQuantity() > p.getQuantity()) {
                            product.setQuantity(p.getQuantity());
                        }
                    },
                    iterator::remove
            );
        }
    }
}

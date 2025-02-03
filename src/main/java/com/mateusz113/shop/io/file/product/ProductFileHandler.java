package com.mateusz113.shop.io.file.product;

public abstract class ProductFileHandler {
    private final String productDetailsPath;

    public ProductFileHandler(String productDetailsPath) {
        this.productDetailsPath = productDetailsPath;
    }

    public String getProductDetailsPath() {
        return productDetailsPath;
    }
}

package com.mateusz113.shop.io.file.product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ProductFileReader extends ProductFileHandler {
    public ProductFileReader(String productDetailsPath) {
        super(productDetailsPath);
    }

    public List<String> getProductsFromFile() throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(getProductDetailsPath()))
        ) {
            return reader
                    .lines()
                    .filter(line -> !line.isBlank())
                    .map(String::trim)
                    .toList();
        }
    }
}

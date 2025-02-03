package com.mateusz113.shop.io.file.product;

import com.mateusz113.shop.io.file.FileWriterInterface;

import java.io.IOException;
import java.util.List;

public class ProductFileWriter extends ProductFileHandler implements FileWriterInterface {
    private boolean append;

    public ProductFileWriter(String productDetailsPath) {
        super(productDetailsPath);
        append = false;
    }

    @Override
    public void shouldAppend(boolean append) {
        this.append = append;
    }

    public void saveShopProductData(List<String> detailsList) throws IOException {
        writeStringListToFile(detailsList, getProductDetailsPath(), append);
    }
}

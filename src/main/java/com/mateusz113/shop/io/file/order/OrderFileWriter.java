package com.mateusz113.shop.io.file.order;

import com.mateusz113.shop.io.file.FileWriterInterface;

import java.io.IOException;
import java.util.List;

public class OrderFileWriter extends OrderFileHandler implements FileWriterInterface {
    private boolean append;

    public OrderFileWriter() {
        append = false;
    }

    @Override
    public void shouldAppend(boolean append) {
        this.append = append;
    }

    public void writeOrderToFile(List<String> sList, String path) throws IOException {
        FileWriterInterface.super.writeStringListToFile(sList, path, append);
    }
}

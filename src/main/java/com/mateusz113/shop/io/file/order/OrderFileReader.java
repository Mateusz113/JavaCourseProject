package com.mateusz113.shop.io.file.order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class OrderFileReader extends OrderFileHandler {
    public List<List<String>> readOldOrders(String userId) throws IOException {
        List<List<String>> oldOrders = new ArrayList<>();
        Path folderPath = Paths.get(String.format("%s/%s", getOrderDetailsFolder(), userId));
        try (
                Stream<Path> filePaths = Files.list(folderPath);
        ) {
            for (Path path : filePaths.toList()) {
                try {
                    oldOrders.add(readOldOrder(path));
                } catch (IOException e) {
                    throw new IOException(String.format("Nie można wczytać pliku ze ścieżki: %s", path.toString()));
                }
            }
        }
        return oldOrders;
    }

    private List<String> readOldOrder(Path path) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(path)))
        ) {
            return reader.lines().toList();
        }
    }
}

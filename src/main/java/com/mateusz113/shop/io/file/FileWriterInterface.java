package com.mateusz113.shop.io.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface FileWriterInterface {
    default void writeStringToFile(String s, String path, boolean append) throws IOException {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, append))
        ) {
            writer.write(s);
            writer.write("\n");
        }
    }

    default void writeStringListToFile(List<String> sList, String path, boolean append) throws IOException {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, append))
        ) {
            for (String s : sList) {
                writer.write(s);
                writer.write("\n");
            }
        }
    }

    void shouldAppend(boolean append);
}

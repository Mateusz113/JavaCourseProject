package com.mateusz113.shop.io;

import com.mateusz113.shop.model.Product;

import java.util.List;

public class ConsolePrinter {
    public static void printLine(String line) {
        System.out.println(line);
    }

    public static void printGreeting() {
        printLine("***** WITAMY W APLIKACJI ELECTRONICS SHOP *****");
    }

    public static void printProducts(List<Product> products) {
        int i = 1;
        for (Product p : products) {
            printLine(i + ".");
            printLine(p.toString());
            i++;
        }
    }

    public static <T> void printMenuOptions(T[] options) {
        for (T option : options) {
            printLine(option.toString());
        }
    }
}

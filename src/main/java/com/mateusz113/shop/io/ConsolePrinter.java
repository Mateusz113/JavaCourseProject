package com.mateusz113.shop.io;

import com.mateusz113.shop.app.menu_navigation.AuthOption;

public class ConsolePrinter {
    public static void printLine(String line) {
        System.out.println(line);
    }

    public static void printGreeting() {
        printLine("***** WITAMY W APLIKACJI ELECTRONICS SHOP *****");
    }

    public static void printAuthOptions() {
        for (AuthOption authOption : AuthOption.values()) {
            printLine(authOption.toString());
        }
    }
}

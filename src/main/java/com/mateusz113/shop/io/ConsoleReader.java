package com.mateusz113.shop.io;

import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.LoginDetails.LoginDetailsBuilder;
import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.auth.RegisterDetails.RegisterDetailsBuilder;
import com.mateusz113.shop.validation.InputValidator;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Consumer;

import static com.mateusz113.shop.io.ConsolePrinter.printLine;

public class ConsoleReader {
    private final Scanner sc;

    public ConsoleReader() {
        sc = new Scanner(System.in);
    }

    public RegisterDetails readRegisterDetails() {
        RegisterDetailsBuilder builder = RegisterDetails.builder();
        printLine("Wprowadź imię:");
        builder.firstName(readStringValueWithValidator(InputValidator::validateName));
        printLine("Wprowadź nazwisko:");
        builder.lastName(readStringValueWithValidator(InputValidator::validateName));
        printLine("Wprowadź email:");
        builder.email(readStringValueWithValidator(InputValidator::validateEmail));
        printLine("Wprowadź hasło:");
        builder.password(readStringValueWithValidator(InputValidator::validatePassword));
        return builder.build();
    }

    public LoginDetails readLoginDetails() {
        LoginDetailsBuilder builder = LoginDetails.builder();
        printLine("Wprowadź email:");
        builder.email(readStringValueWithValidator(InputValidator::validateEmail));
        printLine("Wprowadź hasło:");
        builder.password(readStringValueWithValidator(InputValidator::validatePassword));
        return builder.build();
    }

    public int readIntValue() {
        int value = -1;
        boolean isInputValid = false;
        while (!isInputValid) {
            try {
                value = sc.nextInt();
                isInputValid = true;
            } catch (InputMismatchException e) {
                printLine("Wprowadzony tekst nie jest liczbą! Spróbuj ponownie.");
            } finally {
                sc.nextLine();
            }
        }
        return value;
    }

    private String readStringValueWithValidator(Consumer<String> validator) {
        String value = "";
        boolean isInputValid = false;
        while (!isInputValid) {
            try {
                value = sc.nextLine();
                validator.accept(value);
                isInputValid = true;
            } catch (IllegalArgumentException e) {
                printLine(String.format("%s Spróbuj ponownie.", e.getMessage()));
            }
        }
        return value;
    }
}

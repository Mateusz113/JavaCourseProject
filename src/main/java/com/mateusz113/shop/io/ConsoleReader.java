package com.mateusz113.shop.io;

import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.LoginDetails.LoginDetailsBuilder;
import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.auth.RegisterDetails.RegisterDetailsBuilder;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.laptop.Laptop;
import com.mateusz113.shop.model.laptop.LaptopAccessory;
import com.mateusz113.shop.model.phone.Phone;
import com.mateusz113.shop.model.phone.PhoneAccessory;
import com.mateusz113.shop.validation.InputValidator;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Consumer;

import static com.mateusz113.shop.io.ConsolePrinter.printLine;

public class ConsoleReader {
    private final Scanner sc;

    public ConsoleReader() {
        sc = new Scanner(System.in);
        sc.useLocale(Locale.US);
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

    public void configureProduct(Product chosenProduct) {
        if (chosenProduct instanceof Phone) configurePhone((Phone) chosenProduct);
        if (chosenProduct instanceof Laptop) configureLaptop((Laptop) chosenProduct);
        printLine("Ile sztuk produktu chciałbyś kupić?");
        int input = readIntValue(1, chosenProduct.getQuantity());
        chosenProduct.setQuantity(input);
    }

    private void configurePhone(Phone phone) {
        printLine("Jaki chcesz kolor telefonu?");
        printLine("""
                1. Czarny
                2. Niebieski (+20 zł)
                3. Czerwony (+30 zł)
                4. Złoty (+100 zł)
                """);
        int input = readIntValue(1, 4);
        switch (input) {
            case 1 -> {
                phone.setPhoneColor("Czarny");
            }
            case 2 -> {
                phone.setPhoneColor("Niebieski");
                phone.setPrice(phone.getPrice().add(BigDecimal.valueOf(20)));
            }
            case 3 -> {
                phone.setPhoneColor("Czerwony");
                phone.setPrice(phone.getPrice().add(BigDecimal.valueOf(30)));
            }
            case 4 -> {
                phone.setPhoneColor("Złoty");
                phone.setPrice(phone.getPrice().add(BigDecimal.valueOf(100)));
            }
        }
        printLine("Jaki chcesz rozmiar baterii?");
        printLine("""
                1. 3000 mAh
                2. 4000 mAh (+100 zł)
                3. 5000 mAh (+200 zł)
                4. 6000 mAh (+300 zł)
                """);
        input = readIntValue(1, 4);
        switch (input) {
            case 1 -> {
                phone.setBatterySize(3000);
            }
            case 2 -> {
                phone.setBatterySize(4000);
                phone.setPrice(phone.getPrice().add(BigDecimal.valueOf(100)));
            }
            case 3 -> {
                phone.setBatterySize(5000);
                phone.setPrice(phone.getPrice().add(BigDecimal.valueOf(200)));
            }
            case 4 -> {
                phone.setBatterySize(6000);
                phone.setPrice(phone.getPrice().add(BigDecimal.valueOf(300)));
            }
        }
        for (PhoneAccessory accessory : PhoneAccessory.values()) {
            printLine(String.format("Czy może chciałbyś też: %s", accessory.getPrintName()));
            printLine(String.format("""
                    1. Tak (+%.2f zł)
                    2. Nie
                    """, accessory.getAdditionalPrice()
            ));
            input = readIntValue(1, 2);
            if (input == 1) phone.addAccessory(accessory);
        }
    }

    private void configureLaptop(Laptop laptop) {
        printLine("Ile chcesz pamięci ram?");
        printLine("""
                1. 4 GB
                2. 8 GB (+200 zł)
                3. 16 GB (+500 zł)
                4. 32 GB (+1000 zł)
                """);
        int input = readIntValue(1, 4);
        switch (input) {
            case 1 -> {
                laptop.setRamMemory(4);
            }
            case 2 -> {
                laptop.setRamMemory(8);
                laptop.setPrice(laptop.getPrice().add(BigDecimal.valueOf(200)));
            }
            case 3 -> {
                laptop.setRamMemory(16);
                laptop.setPrice(laptop.getPrice().add(BigDecimal.valueOf(500)));
            }
            case 4 -> {
                laptop.setRamMemory(32);
                laptop.setPrice(laptop.getPrice().add(BigDecimal.valueOf(1000)));
            }
        }
        printLine("Jakie chcesz odświeżanie procesora?");
        printLine("""
                1. 60 Hz
                2. 120 Hz (+200 zł)
                3. 240 Hz (+400 zł)
                4. 320 Hz (+700 zł)
                """);
        input = readIntValue(1, 4);
        switch (input) {
            case 1 -> {
                laptop.setMonitorRefreshRate(60);
            }
            case 2 -> {
                laptop.setMonitorRefreshRate(120);
                laptop.setPrice(laptop.getPrice().add(BigDecimal.valueOf(200)));
            }
            case 3 -> {
                laptop.setMonitorRefreshRate(240);
                laptop.setPrice(laptop.getPrice().add(BigDecimal.valueOf(400)));
            }
            case 4 -> {
                laptop.setMonitorRefreshRate(320);
                laptop.setPrice(laptop.getPrice().add(BigDecimal.valueOf(700)));
            }
        }

        for (LaptopAccessory accessory : LaptopAccessory.values()) {
            printLine(String.format("Czy może chciałbyś też: %s", accessory.getPrintName()));
            printLine(String.format("""
                    1. Tak (+%.2f zł)
                    2. Nie
                    """, accessory.getAdditionalPrice()
            ));
            input = readIntValue(1, 2);
            if (input == 1) laptop.addAccessory(accessory);
        }
    }

    public Product readProductFromKeyboard() {
        printLine("""
                Co chcesz dodać?
                1.Laptop
                2.Telefon
                3.Inny produkt""");
        int type = readIntValue(1, 3);
        printLine("Podaj nazwę produktu:");
        String name = sc.nextLine();
        printLine("Podaj cenę produktu:");
        BigDecimal price = readBigDecimalValue();
        printLine("Podaj ilość sztuk produktu:");
        int quantity = readIntValue();
        Product product = null;
        switch (type) {
            case 1 -> product = readLaptopFromKeyboard(name, price, quantity);
            case 2 -> product = new Phone(UUID.randomUUID().toString(), name, price, quantity);
            case 3 -> product = new Product(UUID.randomUUID().toString(), name, price, quantity);
        }
        return product;
    }

    private Laptop readLaptopFromKeyboard(String name, BigDecimal price, int quantity) {
        printLine("Podaj nazwę procesora:");
        String processor = sc.nextLine();
        printLine("Podaj nazwę karty graficznej:");
        String graphicsCard = sc.nextLine();
        return new Laptop(UUID.randomUUID().toString(), name, price, quantity, processor, graphicsCard);
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

    //Function includes from and to values when boundary checking
    public int readIntValue(int from, int to) {
        int input = Integer.MIN_VALUE;
        boolean inputIsValid = false;
        while (!inputIsValid) {
            try {
                input = sc.nextInt();
                if (input < from || input > to) {
                    printLine(String.format("Wprowadzona liczba musi się mieścić w zakrecie od %d do %d!", from, to));
                } else {
                    inputIsValid = true;
                }
            } catch (InputMismatchException e) {
                printLine("Wprowadzony tekst nie jest liczbą! Spróbuj ponownie.");
            } finally {
                sc.nextLine();
            }
        }
        return input;
    }

    public BigDecimal readBigDecimalValue() {
        BigDecimal input = null;
        boolean inputIsValid = false;
        while (!inputIsValid) {
            try {
                input = sc.nextBigDecimal();
                inputIsValid = true;
            } catch (InputMismatchException e) {
                printLine("Wprowadzony tekst nie jest liczbą, lub użyto nieprawidłowego znaku rozdzielającego części dziesiętne! (Używaj kropki)");
            } finally {
                sc.nextLine();
            }
        }
        return input;
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

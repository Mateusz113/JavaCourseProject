package com.mateusz113.shop.app;

import com.mateusz113.shop.app.menu_navigation.AuthOption;
import com.mateusz113.shop.auth.AuthManager;
import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.exception.NoSuchOptionException;
import com.mateusz113.shop.exception.NoSuchUserException;
import com.mateusz113.shop.exception.UserAlreadyExistsException;
import com.mateusz113.shop.io.ConsoleReader;
import com.mateusz113.shop.io.file.ShopFileReader;
import com.mateusz113.shop.io.file.ShopFileWriter;
import com.mateusz113.shop.model.User;

import java.io.IOException;

import static com.mateusz113.shop.io.ConsolePrinter.*;

public class ShopControl {
    private final String USER_LOGIN_INFO_FILE_PATH = "src/main/resources/data/userLoginInfo.txt";
    private final String USER_DETAILS_FILE_PATH = "src/main/resources/data/userData.txt";
    private final AuthManager authManager;
    private final ConsoleReader consoleReader;
    private final ShopFileReader fileReader;
    private final ShopFileWriter writer;
    private User currentUser;

    public ShopControl() {
        this.consoleReader = new ConsoleReader();
        this.fileReader = new ShopFileReader(USER_LOGIN_INFO_FILE_PATH, USER_DETAILS_FILE_PATH);
        this.writer = new ShopFileWriter(USER_LOGIN_INFO_FILE_PATH, USER_DETAILS_FILE_PATH);
        this.authManager = new AuthManager();
    }

    public void start() {
        printGreeting();
        authLoop();
    }

    public void authLoop() {
        AuthOption option;
        printAuthOptions();
        option = getAuthOption();
        switch (option) {
            case EXIT -> exit();
            case LOGIN -> login();
            case REGISTER -> register();
        }
    }

    public void mainLoop() {
        printLine(String.format("Witaj %s!", currentUser.firstName()));
        exit();
    }

    public void login() {
        LoginDetails details = consoleReader.readLoginDetails();
        printLine(details.toString());
        try {
            currentUser = authManager.loginUser(details, fileReader);
            printLine("Pomyślnie zalogowano.");
            mainLoop();
        } catch (NoSuchUserException e) {
            printLine(e.getMessage());
            authLoop();
        } catch (IOException e) {
            printLine(e.getMessage());
            exit();
        }
    }

    public void register() {
        RegisterDetails details = consoleReader.readRegisterDetails();
        try {
            currentUser = authManager.registerUser(details, writer, fileReader);
            printLine("Pomyślnie zarejestrowano.");
            mainLoop();
        } catch (UserAlreadyExistsException e) {
            printLine(e.getMessage());
            authLoop();
        } catch (IOException e) {
            printLine(e.getMessage());
            exit();
        }
    }

    private void exit() {
        printLine("Zapraszamy ponownie!");
    }

    private AuthOption getAuthOption() {
        boolean optionIsValid = false;
        AuthOption option = null;
        while (!optionIsValid) {
            try {
                int value = consoleReader.readIntValue();
                option = AuthOption.getOptionFromValue(value);
                optionIsValid = true;
            } catch (NoSuchOptionException e) {
                printLine(e.getMessage());
            }
        }
        return option;
    }
}

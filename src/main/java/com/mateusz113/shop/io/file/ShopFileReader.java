package com.mateusz113.shop.io.file;

import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.exception.NoSuchUserException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ShopFileReader extends FileHandler {
    public ShopFileReader(String userLoginInfoPath, String userDetailsPath, String productDetailsPath) {
        super(userLoginInfoPath, userDetailsPath, productDetailsPath);
    }

    public List<LoginDetails> getUsersLoginDetails() throws IOException {
        List<LoginDetails> loginDetails = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(getUserLoginInfoPath()))
        ) {
            reader
                    .lines()
                    .map(line -> line.split(";"))
                    .forEach(data -> {
                        if (data.length > 0) {
                            loginDetails.add(new LoginDetails(data[0], data[1]));
                        }
                    });
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException("Plik z danymi jest w niewłaściwym formacie");
        }
        return loginDetails;
    }

    public String getLoginInfoFromLoginDetails(LoginDetails loginDetails) throws IOException, NoSuchUserException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(getUserLoginInfoPath()))
        ) {
            return reader
                    .lines()
                    .filter(line -> {
                        String[] infoElements = line.split(";");
                        return Objects.equals(infoElements[0], loginDetails.email()) && Objects.equals(infoElements[1], loginDetails.password());
                    })
                    .toList()
                    .getFirst();
        } catch (NoSuchElementException e) {
            throw new NoSuchUserException("Użytkownik o podanych danych nie istnieje!");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException("Plik z danymi jest w niewłaściwym formacie!");
        }
    }

    public String getUserInfoFromId(String id) throws IOException, NoSuchUserException {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(getUserDetailsPath()))
        ) {
            return reader
                    .lines()
                    .filter(line -> Objects.equals(line.split(";")[0], id))
                    .toList()
                    .getFirst();
        } catch (NoSuchElementException e) {
            throw new NoSuchUserException("Użytkownik o podanym id nie istnieje!");
        }
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

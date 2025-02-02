package com.mateusz113.shop.io.file;

import com.mateusz113.shop.auth.RegisterDetails;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class ShopFileWriter extends FileHandler {
    private boolean append;

    public ShopFileWriter(String userLoginInfoPath, String userDetailsPath) {
        super(userLoginInfoPath, userDetailsPath);
        append = true;
    }

    public void setAppendFlag(boolean append) {
        this.append = append;
    }

    public void saveNewUserInformation(RegisterDetails registerDetails, String id) throws IOException {
        try {
            saveUserLoginInfo(registerDetails, id);
            saveUserDetails(registerDetails, id);
        } catch (IOException e) {
            throw new IOException(String.format("Podczas zapisywania nowego użytkownika wystąpił błąd: %s", e.getMessage()));
        }
    }

    private void saveUserLoginInfo(RegisterDetails registerDetails, String id) throws IOException {
        String userLoginInfo = String.format("%s;%s;%s%n", registerDetails.email(), registerDetails.password(), id);
        writeStringToFile(userLoginInfo, getUserLoginInfoPath());
    }

    private void saveUserDetails(RegisterDetails registerDetails, String id) throws IOException {
        String userDetails = String
                .format("%s;%s;%s;%s%n", id, registerDetails.firstName(), registerDetails.lastName(), registerDetails.email());
        writeStringToFile(userDetails, getUserDetailsPath());
    }

    private void writeStringToFile(String s, String path) throws IOException {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, append))
        ) {
            writer.write(s);
        }
    }
}

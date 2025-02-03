package com.mateusz113.shop.io.file.user;

import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.io.file.FileWriterInterface;

import java.io.IOException;

public class UserFileWriter extends UserFileHandler implements FileWriterInterface {
    private boolean append;

    public UserFileWriter(String userLoginInfoPath, String userDetailsPath) {
        super(userLoginInfoPath, userDetailsPath);
        append = true;
    }

    @Override
    public void shouldAppend(boolean append) {
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
        String userLoginInfo = String.format("%s;%s;%s", registerDetails.email(), registerDetails.password(), id);
        writeStringToFile(userLoginInfo, getUserLoginInfoPath(), append);
    }

    private void saveUserDetails(RegisterDetails registerDetails, String id) throws IOException {
        String userDetails = String
                .format("%s;%s;%s;%s", id, registerDetails.firstName(), registerDetails.lastName(), registerDetails.email());
        writeStringToFile(userDetails, getUserDetailsPath(), append);
    }
}

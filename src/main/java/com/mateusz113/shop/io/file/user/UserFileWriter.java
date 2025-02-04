package com.mateusz113.shop.io.file.user;

import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.io.file.FileWriterInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            createUserOrdersFolder(id);
        } catch (IOException e) {
            throw new IOException(String.format("Podczas zapisywania nowego użytkownika wystąpił błąd: %s", e.getMessage()));
        }
    }

    public void createLoginInfoFile() throws IOException{
        Path filePath = Paths.get(getUserLoginInfoPath());
        Files.createFile(filePath);
    }

    public void createUserDetailsFile() throws IOException {
        Path filePath = Paths.get(getUserDetailsPath());
        Files.createFile(filePath);
    }

    private void createUserOrdersFolder(String id) throws IOException {
        try {
            Path orderFolderPath = Paths.get(String.format("src/main/resources/data/%s", id));
            Files.createDirectories(orderFolderPath);
        } catch (IOException e) {
            throw new IOException("Nie udało się stworzyć folderu na dane o zamówieniach użytkownika.");
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

package com.mateusz113.shop.auth;

import com.mateusz113.shop.exception.NoSuchUserException;
import com.mateusz113.shop.exception.UserAlreadyExistsException;
import com.mateusz113.shop.io.file.ShopFileReader;
import com.mateusz113.shop.io.file.ShopFileWriter;
import com.mateusz113.shop.model.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AuthManager {
    public User registerUser(RegisterDetails registerDetails, ShopFileWriter writer, ShopFileReader reader) throws UserAlreadyExistsException, IOException {
        List<String> usedEmails = reader.getUsersLoginDetails().stream().map(LoginDetails::email).toList();
        boolean emailInUse = usedEmails.stream().anyMatch(usedEmail -> usedEmail.equals(registerDetails.email()));
        if (emailInUse) throw new UserAlreadyExistsException("Użytkownik z takim adresem email już istnieje!");
        String id = UUID.randomUUID().toString();
        writer.saveNewUserInformation(registerDetails, id);
        return new User(id, registerDetails.firstName(), registerDetails.lastName(), registerDetails.email());
    }

    public User loginUser(LoginDetails loginDetails, ShopFileReader reader) throws IOException, NoSuchUserException {
        String userId = getIdFromLoginDetails(loginDetails, reader);
        return getUserFromId(userId, reader);
    }

    private String getIdFromLoginDetails(LoginDetails loginDetails, ShopFileReader reader) throws IOException, NoSuchUserException {
        String loginInfo = reader.getLoginInfoFromLoginDetails(loginDetails);
        try {
            return loginInfo.split(";")[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException("Plik z danymi jest w niewłaściwym formacie!");
        }
    }

    private User getUserFromId(String id, ShopFileReader reader) throws IOException, NoSuchUserException {
        String compactedDetails = reader.getUserInfoFromId(id);
        String[] details = compactedDetails.split(";");
        try {
            return new User(details[0], details[1], details[2], details[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IOException("Plik z danymi jest w niewłaściwym formacie!");
        }
    }
}

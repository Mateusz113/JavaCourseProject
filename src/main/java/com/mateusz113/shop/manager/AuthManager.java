package com.mateusz113.shop.manager;

import com.mateusz113.shop.auth.LoginDetails;
import com.mateusz113.shop.auth.RegisterDetails;
import com.mateusz113.shop.exception.NoSuchUserException;
import com.mateusz113.shop.exception.UserAlreadyExistsException;
import com.mateusz113.shop.mapper.RegisterDetailsMapper;
import com.mateusz113.shop.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Class that holds the user's data and manages new registers.
 */
public class AuthManager implements Serializable {
    private final List<User> users = new ArrayList<>();
    private final List<LoginDetails> usersLoginDetails = new ArrayList<>();

    /**
     * Registers new user into the system.
     *
     * @param registerDetails details of the register.
     * @return {@code User} object created from data provided.
     * @throws UserAlreadyExistsException if the user with provided email already exists.
     */
    public User registerUser(RegisterDetails registerDetails) throws UserAlreadyExistsException {
        if (usersLoginDetails.stream().anyMatch(loginDetails -> registerDetails.email().equals(loginDetails.email()))) {
            throw new UserAlreadyExistsException("Użytkownik z takim adresem email już istnieje!");
        }
        String id = UUID.randomUUID().toString();
        User newUser = new User(id, registerDetails.firstName(), registerDetails.lastName(), registerDetails.email());
        usersLoginDetails.add(RegisterDetailsMapper.toLoginDetails(registerDetails));
        users.add(newUser);
        return newUser;
    }

    /**
     * Logins the user into the system.
     *
     * @param loginDetails details of the login.
     * @return {@code User} object retrieved from the list, that match the provided data.
     * @throws NoSuchUserException if there is no user that matches provided {@code LoginDetails}.
     */
    public User loginUser(LoginDetails loginDetails) throws NoSuchUserException {
        if (usersLoginDetails.stream().noneMatch(details -> details.equals(loginDetails))) {
            throw new NoSuchUserException("Użytkownik z takimi danymi nie istnieje!");
        }
        Optional<User> matchingUser = users.stream().filter(user -> user.email().equals(loginDetails.email())).findFirst();
        if (matchingUser.isEmpty()) {
            throw new NoSuchUserException("Użytkownika z takim adresem email nie ma w bazie danych!");
        }
        return matchingUser.get();
    }
}

package com.mateusz113.shop.validation;

import java.util.regex.Pattern;

/**
 * Class that holds the input validation functions.
 */
public class InputValidator {
    /**
     * Checks if the provided email is valid.
     * Email must follow format {@code firstPart@domain.countryCode}.
     *
     * @param email email input.
     * @throws IllegalArgumentException if the email provided is invalid.
     */
    public static void validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        if (email.isBlank()) throw new IllegalArgumentException("Email nie może być pusty!");
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email jest niepoprawny! Proszę wprowadzić email w prawidłowym formacie (np. email@domena.pl).");
        }
    }

    /**
     * Checks if the provided password is valid.
     * Passwords must be at least eight characters long.
     *
     * @param password password input.
     * @throws IllegalArgumentException if the password provided is invalid.
     */
    public static void validatePassword(String password) {
        if (password.isBlank()) throw new IllegalArgumentException("Hasło nie może być puste!");
        if (password.trim().length() < 8) throw new IllegalArgumentException("Hasło musi mieć co najmniej 8 znaków!");
    }


    /**
     * Checks if the provided name is valid.
     * Used to check for the validity of the user first and last name.
     * Name must start will a big letter followed by small ones.
     *
     * @param name name input.
     * @throws IllegalArgumentException if the name provided is invalid.
     */
    public static void validateName(String name) {
        Pattern namePattern = Pattern.compile("^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]+$");
        if (name.isBlank()) throw new IllegalArgumentException("Imię/Nazwisko nie może być puste!");
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Imię/Nazwisko może zawierać tylko litery i zaczynać się z dużej litery!");
        }
    }
}

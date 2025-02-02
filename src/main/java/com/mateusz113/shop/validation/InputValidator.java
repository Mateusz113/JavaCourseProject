package com.mateusz113.shop.validation;

import java.util.regex.Pattern;

public class InputValidator {
    public static void validateEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        if (email.isBlank()) throw new IllegalArgumentException("Email nie może być pusty!");
        if (!emailPattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Email jest niepoprawny! Proszę wprowadzić email w prawidłowym formacie (np. email@domena.pl).");
        }
    }

    public static void validatePassword(String password) {
        if (password.isBlank()) throw new IllegalArgumentException("Hasło nie może być puste!");
        if (password.trim().length() < 8) throw new IllegalArgumentException("Hasło musi mieć co najmniej 8 znaków!");
    }

    public static void validateName(String name) {
        Pattern namePattern = Pattern.compile("^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]+$");
        if (name.isBlank()) throw new IllegalArgumentException("Imię/Nazwisko nie może być puste!");
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Imię/Nazwisko może zawierać tylko litery i zaczynać się z dużej litery!");
        }
    }
}

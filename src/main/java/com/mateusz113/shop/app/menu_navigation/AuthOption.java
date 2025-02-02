package com.mateusz113.shop.app.menu_navigation;

import com.mateusz113.shop.exception.NoSuchOptionException;

public enum AuthOption {
    EXIT(0, "Opuść program"), REGISTER(1, "Rejestracja"), LOGIN(2, "Login");
    private int value;
    private String label;

    AuthOption(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", value, label);
    }

    public static AuthOption getOptionFromValue(int value) throws NoSuchOptionException {
        try {
            return AuthOption.values()[value];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchOptionException(String.format("Opcja %d nie istnieje!", value));
        }
    }
}

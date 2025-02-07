package com.mateusz113.shop.app.menu_navigation;

import com.mateusz113.shop.exception.NoSuchOptionException;

/**
 * Class representing options in app authentication loop.
 */
public enum AuthOption {
    EXIT(0, "Opuść program"), REGISTER(1, "Rejestracja"), LOGIN(2, "Login");
    private int value;
    private String label;

    /**
     * Constructor for the authentication option.
     *
     * @param value numerical value of the option.
     * @param label label describing the option to the user.
     */
    AuthOption(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", value, label);
    }

    /**
     * Retrieves {@code AuthOption} from the numerical value.
     *
     * @param value numerical value of the authentication option.
     * @return the corresponding {@code AuthOption}
     * @throws NoSuchOptionException if the provided value has no associated option.
     */
    public static AuthOption getOptionFromValue(int value) throws NoSuchOptionException {
        try {
            return AuthOption.values()[value];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchOptionException(String.format("Opcja %d nie istnieje!", value));
        }
    }
}

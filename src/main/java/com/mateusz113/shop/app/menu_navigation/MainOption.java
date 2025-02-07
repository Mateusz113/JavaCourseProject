package com.mateusz113.shop.app.menu_navigation;

import com.mateusz113.shop.exception.NoSuchOptionException;

/**
 * Class representing options in app main loop.
 */
public enum MainOption {
    EXIT(0, "Opuść program"),
    SHOP(1, "Kup produkt"),
    MANAGE_PRODUCTS(2, "Zarządzaj produktami"),
    CART(3, "Koszyk"),
    PREVIOUS_ORDERS(4, "Poprzednie zamówienia");
    private int value;
    private String label;

    /**
     * Constructor for the main option.
     *
     * @param value numerical value associated with the option.
     * @param label label describing option to the user.
     */
    MainOption(int value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String toString() {
        return String.format("%d. %s", value, label);
    }

    /**
     * Retrieves {@code MainOption} based on the numerical value.
     *
     * @param value numerical value associated with the option.
     * @return the corresponding {@code MainOption}.
     * @throws NoSuchOptionException if provided value has no associated option.
     */
    public static MainOption getOptionFromValue(int value) throws NoSuchOptionException {
        try {
            return MainOption.values()[value];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchOptionException(String.format("Opcja %d nie istnieje!", value));
        }
    }
}

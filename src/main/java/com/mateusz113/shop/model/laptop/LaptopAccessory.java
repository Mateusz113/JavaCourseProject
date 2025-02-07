package com.mateusz113.shop.model.laptop;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Enum that represents the accessory available to laptops.
 */
public enum LaptopAccessory implements Serializable {
    LAPTOP_BAG("Torba na laptopa", new BigDecimal(50)),
    WIRELESS_MOUSE("Bezprzewodowa myszka", new BigDecimal(40));

    private final String printName;
    private final BigDecimal additionalPrice;

    /**
     * Constructor of the laptop accessory enum.
     *
     * @param printName label that will be presented to user.
     * @param additionalPrice additional price that this accessory will cost.
     */
    LaptopAccessory(String printName, BigDecimal additionalPrice) {
        this.printName = printName;
        this.additionalPrice = additionalPrice;
    }

    public String getPrintName() {
        return printName;
    }

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }
}

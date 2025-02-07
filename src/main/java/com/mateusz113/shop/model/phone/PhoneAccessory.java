package com.mateusz113.shop.model.phone;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Enum that represents accessories available to phones.
 */
public enum PhoneAccessory implements Serializable {
    CASE("Etui", new BigDecimal(10)),
    BRAIDED_CABLE("Pleciony kabel", new BigDecimal(15));

    private final String printName;
    private final BigDecimal additionalPrice;

    /**
     * Constructor of the phone accessory enum.
     *
     * @param printName label that will be presented to the user.
     * @param additionalPrice additional price this accessory will cost.
     */
    PhoneAccessory(String printName, BigDecimal additionalPrice) {
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

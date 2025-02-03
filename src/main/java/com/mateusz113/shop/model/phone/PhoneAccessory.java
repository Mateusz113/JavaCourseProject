package com.mateusz113.shop.model.phone;

import java.math.BigDecimal;

public enum PhoneAccessory {
    CASE("Etui", new BigDecimal(10)),
    BRAIDED_CABLE("Pleciony kabel", new BigDecimal(15));

    private final String printName;
    private final BigDecimal additionalPrice;

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

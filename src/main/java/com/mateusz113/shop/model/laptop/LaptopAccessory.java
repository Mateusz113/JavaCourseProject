package com.mateusz113.shop.model.laptop;

import java.math.BigDecimal;

public enum LaptopAccessory {
    LAPTOP_BAG("Torba na laptopa", new BigDecimal(50)),
    WIRELESS_MOUSE("Bezprzewodowa myszka", new BigDecimal(40));

    private final String printName;
    private final BigDecimal additionalPrice;

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

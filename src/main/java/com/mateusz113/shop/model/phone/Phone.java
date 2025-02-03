package com.mateusz113.shop.model.phone;

import com.mateusz113.shop.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Phone extends Product {
    private String phoneColor;
    private int batterySize;
    private List<PhoneAccessory> accessories;

    public Phone(String id, String name, BigDecimal price, int quantity) {
        super(id, name, price, quantity);
        this.phoneColor = "Czarny";
        this.batterySize = 4000;
        this.accessories = new ArrayList<>();
    }

    public String getPhoneColor() {
        return phoneColor;
    }

    public void setPhoneColor(String phoneColor) {
        this.phoneColor = phoneColor;
    }

    public int getBatterySize() {
        return batterySize;
    }

    public void setBatterySize(int batterySize) {
        this.batterySize = batterySize;
    }

    public List<PhoneAccessory> getAccessories() {
        return accessories;
    }

    public void addAccessory(PhoneAccessory pa) {
        accessories.add(pa);
    }

    @Override
    public String toString() {
        return String.format("""
                Nazwa: %s
                Cena: %.2f zł
                Kolor: %s
                Pojemność baterii: %d
                Dostępna ilość: %d
                """, getName(), getPrice(), phoneColor, batterySize, getQuantity());
    }
}

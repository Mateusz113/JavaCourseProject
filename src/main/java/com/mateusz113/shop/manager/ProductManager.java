package com.mateusz113.shop.manager;

import com.mateusz113.shop.exception.IllegalFormatException;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.laptop.Laptop;
import com.mateusz113.shop.model.laptop.LaptopAccessory;
import com.mateusz113.shop.model.phone.Phone;
import com.mateusz113.shop.model.phone.PhoneAccessory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductManager {
    public List<Product> constructProductListFromDetails(List<String> detailsList) throws IllegalFormatException {
        List<Product> products = new ArrayList<>();
        for (String details : detailsList) {
            products.add(constructProductFromDetails(details));
        }
        return products;
    }

    public Product constructProductFromDetails(String details) throws IllegalFormatException {
        Product product = null;
        try {
            String[] detailsSplit = details.split(";");
            switch (detailsSplit[1]) {
                case "product" -> product = getProductFromDetails(detailsSplit);
                case "phone" -> product = getPhoneFromDetails(detailsSplit);
                case "laptop" -> product = getLaptopFromDetails(detailsSplit);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalFormatException("Plik jest w niewłaściwym formacie!");
        }
        return product;
    }

    private Product getProductFromDetails(String[] details) {
        String id = details[0];
        String name = details[2];
        BigDecimal price = new BigDecimal(details[3]);
        int quantity = Integer.parseInt(details[4]);
        return new Product(id, name, price, quantity);
    }

    private Phone getPhoneFromDetails(String[] details) {
        Product p = getProductFromDetails(details);
        Phone phone = new Phone(p.getId(), p.getName(), p.getPrice(), p.getQuantity());
        String color = details[5];
        phone.setPhoneColor(color);
        int batterySize = Integer.parseInt(details[6]);
        phone.setBatterySize(batterySize);
        for (int i = 7; i < details.length; i++) {
            phone.addAccessory(PhoneAccessory.valueOf(details[i]));
        }
        return phone;
    }

    private Laptop getLaptopFromDetails(String[] details) {
        Product p = getProductFromDetails(details);
        String processor = details[5];
        String graphicsCard = details[6];
        Laptop laptop = new Laptop(p.getId(), p.getName(), p.getPrice(), p.getQuantity(), processor, graphicsCard);
        int ramMemory = Integer.parseInt(details[7]);
        laptop.setRamMemory(ramMemory);
        int monitorRefreshRate = Integer.parseInt(details[8]);
        laptop.setMonitorRefreshRate(monitorRefreshRate);
        for (int i = 9; i < details.length; i++) {
            laptop.addAccessory(LaptopAccessory.valueOf(details[i]));
        }
        return laptop;
    }
}

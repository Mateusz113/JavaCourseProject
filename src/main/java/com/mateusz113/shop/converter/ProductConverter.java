package com.mateusz113.shop.converter;

import com.mateusz113.shop.exception.IllegalFormatException;
import com.mateusz113.shop.model.Product;
import com.mateusz113.shop.model.laptop.Laptop;
import com.mateusz113.shop.model.laptop.LaptopAccessory;
import com.mateusz113.shop.model.phone.Phone;
import com.mateusz113.shop.model.phone.PhoneAccessory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductConverter {
    private final String PRODUCT_TAG = "product";
    private final String PHONE_TAG = "phone";
    private final String LAPTOP_TAG = "laptop";

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
                case PHONE_TAG -> product = getPhoneFromDetails(detailsSplit);
                case LAPTOP_TAG -> product = getLaptopFromDetails(detailsSplit);
                case PRODUCT_TAG -> product = getProductFromDetails(detailsSplit);
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

    public List<String> constructDetailsListFromProducts(List<Product> products) {
        List<String> detailsList = new ArrayList<>();
        for (Product product : products) {
            detailsList.add(constructDetailsFromProduct(product));
        }
        return detailsList;
    }

    public String constructDetailsFromProduct(Product p) {
        String details;
        switch (p) {
            case Phone phone -> details = getDetailsFromPhone(phone);
            case Laptop laptop -> details = getDetailsFromLaptop(laptop);
            case Product product -> details = getDetailsFromProduct(product);
        }
        return details;
    }

    private String getDetailsFromProduct(Product product) {
        List<String> itemsToAppend = List.of(
                product.getId(),
                PRODUCT_TAG,
                product.getName(),
                product.getPrice().toString(),
                String.valueOf(product.getQuantity())
        );
        return getStringFromItemsToAppend(itemsToAppend);
    }


    private String getDetailsFromPhone(Phone phone) {
        List<String> itemsToAppend = new ArrayList<>(
                List.of(
                        phone.getId(),
                        PHONE_TAG,
                        phone.getName(),
                        phone.getPrice().toString(),
                        String.valueOf(phone.getQuantity()),
                        phone.getPhoneColor(),
                        String.valueOf(phone.getBatterySize())
                )
        );
        for (PhoneAccessory phoneAccessory : phone.getAccessories()) {
            itemsToAppend.add(phoneAccessory.toString());
        }
        return getStringFromItemsToAppend(itemsToAppend);
    }

    private String getDetailsFromLaptop(Laptop laptop) {
        List<String> itemsToAppend = new ArrayList<>(
                List.of(
                        laptop.getId(),
                        LAPTOP_TAG,
                        laptop.getName(),
                        laptop.getPrice().toString(),
                        String.valueOf(laptop.getQuantity()),
                        laptop.getProcessor(),
                        laptop.getGraphicsCard(),
                        String.valueOf(laptop.getRamMemory()),
                        String.valueOf(laptop.getMonitorRefreshRate())
                )
        );
        for (LaptopAccessory laptopAccessory : laptop.getAccessories()) {
            itemsToAppend.add(laptopAccessory.toString());
        }
        return getStringFromItemsToAppend(itemsToAppend);
    }

    private String getStringFromItemsToAppend(List<String> itemsToAppend) {
        StringBuilder sb = new StringBuilder();
        String template = "%s;";
        for (int i = 0; i < itemsToAppend.size(); i++) {
            if (i == itemsToAppend.size() - 1) {
                sb.append(itemsToAppend.get(i));
                continue;
            }
            sb.append(template.formatted(itemsToAppend.get(i)));
        }
        return sb.toString();
    }
}

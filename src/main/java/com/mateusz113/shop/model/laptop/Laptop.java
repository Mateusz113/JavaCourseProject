package com.mateusz113.shop.model.laptop;

import com.mateusz113.shop.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Subclass of the {@code Product} that holds additional laptop information.
 */
public class Laptop extends Product {
    private final String processor;
    private final String graphicsCard;
    private int ramMemory;
    private int monitorRefreshRate;
    private List<LaptopAccessory> accessories;

    /**
     * Constructor of the {@code Laptop} class.
     *
     * @param id ID of the laptop.
     * @param name name of the laptop.
     * @param price price of the laptop.
     * @param quantity quantity of the laptop.
     * @param processor processor that is inside the laptop.
     * @param graphicsCard graphics card that is inside the laptop.
     */
    public Laptop(String id, String name, BigDecimal price, int quantity, String processor, String graphicsCard) {
        super(id, name, price, quantity);
        this.processor = processor;
        this.graphicsCard = graphicsCard;
        this.ramMemory = 4;
        this.monitorRefreshRate = 60;
        this.accessories = new ArrayList<>();
    }

    public String getProcessor() {
        return processor;
    }

    public String getGraphicsCard() {
        return graphicsCard;
    }

    public int getRamMemory() {
        return ramMemory;
    }

    public void setRamMemory(int ramMemory) {
        this.ramMemory = ramMemory;
    }

    public int getMonitorRefreshRate() {
        return monitorRefreshRate;
    }

    public void setMonitorRefreshRate(int monitorRefreshRate) {
        this.monitorRefreshRate = monitorRefreshRate;
    }

    public List<LaptopAccessory> getAccessories() {
        return accessories;
    }

    public void addAccessory(LaptopAccessory la) {
        accessories.add(la);
    }

    @Override
    public String toString() {
        return String.format("""
                Nazwa: %s
                Cena: %.2f zł
                Procesor: %s
                Karta graficzna: %s
                Pamięć ram: %d GB
                Częstotliwość odświeżania monitora: %d Hz
                Akcesoria: %s
                Ilość: %d
                """, getName(), getPrice(), processor, graphicsCard, ramMemory, monitorRefreshRate, getAccessoriesAsString(), getQuantity());
    }

    private String getAccessoriesAsString() {
        return accessories.stream().map(LaptopAccessory::getPrintName).collect(Collectors.joining(", "));
    }
}

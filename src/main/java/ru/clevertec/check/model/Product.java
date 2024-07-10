package ru.clevertec.check.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Product {
    private long id;
    private String description;
    private double price;
    private int quantity;
    @JsonProperty("isWholesale")
    private boolean isWholesale;

    public Product() {
    }

    public Product(String description, double price, int quantity, boolean isWholesale) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.isWholesale = isWholesale;
    }

    public Product(long id, String description, double price, int quantity, boolean isWholesale) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.isWholesale = isWholesale;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isWholesale() {
        return isWholesale;
    }

    public void setWholesale(boolean wholesale) {
        this.isWholesale = wholesale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Double.compare(product.price, price) == 0 &&
                quantity == product.quantity &&
                isWholesale == product.isWholesale &&
                Objects.equals(description, product.description);
    }
}

package ru.clevertec.check.dto;


public class PaySlip {
    private int quantity;
    private String description;
    private double price;
    private double discount;
    private double total;

    public PaySlip() {
    }

    public PaySlip(int quantity, String description, double price, double discount, double total) {
        this.quantity = quantity;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.total = total;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}

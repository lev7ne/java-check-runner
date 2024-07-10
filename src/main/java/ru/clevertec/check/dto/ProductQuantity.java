package ru.clevertec.check.dto;

public class ProductQuantity {
    private long id;
    private int quantity;

    public ProductQuantity() {
    }

    public ProductQuantity(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

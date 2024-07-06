package main.java.ru.clevertec.check.model;

public class Product {
    private final int id;
    private final String description;
    private final double price;
    private final int quantityInStock;
    private final boolean wholesaleProduct;

    public Product(int id, String description, double price, int quantityInStock, boolean wholesaleProduct) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.wholesaleProduct = wholesaleProduct;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public boolean isWholesaleProduct() {
        return wholesaleProduct;
    }

    @Override
    public String toString() {
        return id + ";" + description + ";" + price + ";" + quantityInStock + ";" + (wholesaleProduct ? "+" : "-");
    }
}

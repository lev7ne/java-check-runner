package ru.clevertec.check.model;

public class DiscountCard {
    private long id;
    private int numberDiscount;
    private int amount;

    public DiscountCard(int id) {
        this.id = id;
    }

    public int getNumberDiscount() {
        return numberDiscount;
    }

    public int getAmount() {
        return amount;
    }

    public DiscountCard(long id, int numberDiscount, int amount) {
        this.id = id;
        this.numberDiscount = numberDiscount;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return numberDiscount + ";" + amount + "%";
    }
}

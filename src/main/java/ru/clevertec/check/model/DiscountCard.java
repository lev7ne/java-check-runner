package main.java.ru.clevertec.check.model;

public class DiscountCard {
    private int id;
    private int numberDiscount;
    private int amount;

    public DiscountCard() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberDiscount() {
        return numberDiscount;
    }

    public void setNumberDiscount(int numberDiscount) {
        this.numberDiscount = numberDiscount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public DiscountCard(int id, int numberDiscount, int amount) {
        this.id = id;
        this.numberDiscount = numberDiscount;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return numberDiscount + ";" + amount + "%";
    }
}

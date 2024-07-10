package ru.clevertec.check.model;

import java.util.Objects;

public class DiscountCard {
    private long id;
    private int discountCard;
    private int discountAmount;

    public DiscountCard() {
    }

    public DiscountCard(int discountCard, int discountAmount) {
        this.discountCard = discountCard;
        this.discountAmount = discountAmount;
    }

    public DiscountCard(long id, int discountCard, int discountAmount) {
        this.id = id;
        this.discountCard = discountCard;
        this.discountAmount = discountAmount;
    }

    public int getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(int discountCard) {
        this.discountCard = discountCard;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountCard card = (DiscountCard) o;
        return Objects.equals(id, card.id) &&
                discountCard == card.discountCard &&
                discountAmount == card.discountAmount;
    }
}

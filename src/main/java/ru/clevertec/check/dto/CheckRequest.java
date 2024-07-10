package ru.clevertec.check.dto;

import java.util.List;

public class CheckRequest {
    private List<ProductQuantity> products;
    private Integer discountCard;
    private int balanceDebitCard;

    public CheckRequest() {
    }

    public CheckRequest(List<ProductQuantity> products, int discountCard, int balanceDebitCard) {
        this.products = products;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
    }

    public List<ProductQuantity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductQuantity> products) {
        this.products = products;
    }

    public Integer getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(int discountCard) {
        this.discountCard = discountCard;
    }

    public int getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public void setBalanceDebitCard(int balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }
}

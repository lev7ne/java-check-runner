package main.java.ru.clevertec.check.dto;


import java.util.Map;

public class ArgsContext {
    private Map<Integer, Integer> purchases;
    private Integer discountCard;
    private Double balanceDebitCard;

    public ArgsContext() {
    }

    public ArgsContext(Map<Integer, Integer> purchases, Integer discountCard, Double balanceDebitCard) {
        this.purchases = purchases;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
    }

    public Map<Integer, Integer> getPurchases() {
        return purchases;
    }

    public Integer getDiscountCard() {
        return discountCard;
    }

    public Double getBalanceDebitCard() {
        return balanceDebitCard;
    }
}

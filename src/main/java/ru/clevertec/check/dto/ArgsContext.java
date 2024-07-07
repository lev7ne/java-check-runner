package ru.clevertec.check.dto;

import java.util.Map;

public class ArgsContext {
    private Map<Integer, Integer> purchases;
    private Integer discountCard;
    private Double balanceDebitCard;
    private String saveToFile;
    private Map<String, String> datasource;

    public ArgsContext(
            Map<Integer, Integer> purchases,
            Integer discountCard,
            Double balanceDebitCard,
            String saveToFile,
            Map<String, String> datasource
    ) {

        this.purchases = purchases;
        this.discountCard = discountCard;
        this.balanceDebitCard = balanceDebitCard;
        this.saveToFile = saveToFile;
        this.datasource = datasource;
    }

    public Map<String, String> getDatasource() {
        return datasource;
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

    public String getSaveToFile() {
        return saveToFile;
    }
}

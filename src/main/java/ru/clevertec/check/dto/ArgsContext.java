package main.java.ru.clevertec.check.dto;


import java.util.Map;

public class ArgsContext {
    private Map<Integer, Integer> purchases;
    private Integer discountCardNumber;
    private Double balanceDebitCard;
    private String pathToFile;
    private String saveToFile;

    public ArgsContext() {
    }

    public ArgsContext(
            Map<Integer, Integer> purchases,
            Integer discountCardNumber,
            Double balanceDebitCard,
            String pathToFile,
            String saveToFile
    ) {
        this.purchases = purchases;
        this.discountCardNumber = discountCardNumber;
        this.balanceDebitCard = balanceDebitCard;
        this.pathToFile = pathToFile;
        this.saveToFile = saveToFile;
    }

    public Map<Integer, Integer> getPurchases() {
        return purchases;
    }

    public void setPurchases(Map<Integer, Integer> purchases) {
        this.purchases = purchases;
    }

    public Integer getDiscountCardNumber() {
        return discountCardNumber;
    }

    public void setDiscountCardNumber(Integer discountCardNumber) {
        this.discountCardNumber = discountCardNumber;
    }

    public Double getBalanceDebitCard() {
        return balanceDebitCard;
    }

    public void setBalanceDebitCard(Double balanceDebitCard) {
        this.balanceDebitCard = balanceDebitCard;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public String getSaveToFile() {
        return saveToFile;
    }

    public void setSaveToFile(String saveToFile) {
        this.saveToFile = saveToFile;
    }
}

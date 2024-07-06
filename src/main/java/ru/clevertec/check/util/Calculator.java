package main.java.ru.clevertec.check.util;

import main.java.ru.clevertec.check.dto.PaySlip;
import main.java.ru.clevertec.check.exception.NotEnoughMoneyException;
import main.java.ru.clevertec.check.model.DiscountCard;
import main.java.ru.clevertec.check.model.Product;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Calculator {

    /**
     * Утилитарный метод для вычисления стоимости каждой позиции и подготовке к записи
     *
     * @param purchases - список покупок
     * @param products - список продуктов
     * @param discountCard - дисконтная карта
     * @param balanceDebitCard - баланс дебетовой карты
     * @return List<PaySlip> - список покупок, сгруппированный для записи в чек
     * @throws NotEnoughMoneyException - метод бросает исключение с сообщением "NOT ENOUGH MONEY" для записи в чек
     */
    public static List<PaySlip> purchaseCalculate(
            Map<Integer, Integer> purchases,
            List<Product> products,
            DiscountCard discountCard,
            double balanceDebitCard
    ) throws NotEnoughMoneyException {

        List<PaySlip> paySlips = new ArrayList<>();
        double sum = 0.00;
        double discount = 1 - (discountCard.getAmount() / 100.0);

        for (Map.Entry<Integer, Integer> entry : purchases.entrySet()) {
            int id = entry.getKey();
            int quantity = entry.getValue();

            Product matchingProduct = null;
            for (Product product : products) {
                if (product.getId() == id) {
                    matchingProduct = product;
                    break;
                }
            }

            if (matchingProduct != null) {
                int quantityInStock = matchingProduct.getQuantityInStock();

                if (quantityInStock >= quantity) {
                    double price = matchingProduct.getPrice();
                    double total;
                    double discountAmount;

                    if (quantity >= 5 && matchingProduct.isWholesaleProduct()) {
                        total = quantity * price * 0.90;
                    } else {
                        total = quantity * price * discount;
                    }
                    discountAmount = (quantity * price) - total;

                    sum += total;

                    // Проверка на превышение баланса дебетовой карты
                    if (sum > balanceDebitCard) {
                        System.out.println("Недостаточно средств для оплаты");
                        throw new NotEnoughMoneyException("NOT ENOUGH MONEY");
                    }

                    // Найти существующий PaySlip для продукта
                    boolean found = false;
                    for (PaySlip paySlip : paySlips) {
                        if (paySlip.getDescription().equals(matchingProduct.getDescription())) {
                            paySlip.setQuantity(paySlip.getQuantity() + quantity);
                            paySlip.setTotal(paySlip.getTotal() + total);
                            paySlip.setDiscount(paySlip.getDiscount() + discountAmount);
                            found = true;
                            break;
                        }
                    }

                    // Если не найдено, создать новый PaySlip
                    if (!found) {
                        PaySlip paySlip = new PaySlip();
                        paySlip.setDescription(matchingProduct.getDescription());
                        paySlip.setQuantity(quantity);
                        paySlip.setPrice(price);
                        paySlip.setTotal(total);
                        paySlip.setDiscount(discountAmount);
                        paySlips.add(paySlip);
                    }
                }
            }
        }

        return paySlips;
    }
}

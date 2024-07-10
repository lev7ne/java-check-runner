package ru.clevertec.check.util;

import ru.clevertec.check.dto.PaySlip;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Calculator {

    public static List<PaySlip> purchaseCalculate(
            Map<Long, Integer> purchases,
            List<Product> products,
            DiscountCard discountCard,
            double balanceDebitCard
    ) throws BadRequestException, NotEnoughMoneyException {

        var amount = 0;
        if (discountCard != null) {
            amount = discountCard.getDiscountAmount();
        }

        List<PaySlip> paySlips = new ArrayList<>();
        double sum = 0.00;
        double discount = 1 - (amount / 100.0);

        Map<Long, Product> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), product);
        }

        for (Map.Entry<Long, Integer> entry : purchases.entrySet()) {
            var matchingProduct = productMap.get(entry.getKey());
            if (matchingProduct == null || matchingProduct.getQuantity() < entry.getValue()) {
                // На складе нет запрашиваемого товара в необходимом количестве
                throw new BadRequestException("BAD REQUEST");
            }
        }

        for (Map.Entry<Long, Integer> entry : purchases.entrySet()) {
            long id = entry.getKey();
            int quantity = entry.getValue();

            var matchingProduct = productMap.get(id);
            if (matchingProduct != null) {
                double price = matchingProduct.getPrice();
                double total = calculateTotal(quantity, price, discount, matchingProduct.isWholesale());
                double discountAmount = (quantity * price) - total;

                sum += total;

                if (sum > balanceDebitCard) {
                    // Недостаточно средств для покупки
                    throw new NotEnoughMoneyException("NOT ENOUGH MONEY");
                }
                addOrUpdatePaySlip(paySlips, matchingProduct, quantity, total, discountAmount);
            }
        }

        return paySlips;
    }

    private static double calculateTotal(int quantity, double price, double discount, boolean isWholesaleProduct) {
        if (quantity >= 5 && isWholesaleProduct) {
            return quantity * price * 0.90;
        } else {
            return quantity * price * discount;
        }
    }

    private static void addOrUpdatePaySlip(List<PaySlip> paySlips, Product product, int quantity, double total, double discountAmount) {
        for (PaySlip paySlip : paySlips) {
            if (paySlip.getDescription().equals(product.getDescription())) {
                paySlip.setQuantity(paySlip.getQuantity() + quantity);
                paySlip.setTotal(paySlip.getTotal() + total);
                paySlip.setDiscount(paySlip.getDiscount() + discountAmount);
                return;
            }
        }
        PaySlip paySlip = new PaySlip();
        paySlip.setDescription(product.getDescription());
        paySlip.setQuantity(quantity);
        paySlip.setPrice(product.getPrice());
        paySlip.setTotal(total);
        paySlip.setDiscount(discountAmount);
        paySlips.add(paySlip);
    }
}
package ru.clevertec.check.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.dto.PaySlip;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Mock
    private Writer mockWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulPurchase() throws NotEnoughMoneyException {
        List<Product> products = List.of(
                new Product(1, "Product A", 10.0, 10, false),
                new Product(2, "Product B", 20.0, 5, true)
        );
        Map<Integer, Integer> purchases = new HashMap<>();
        purchases.put(1, 2);
        purchases.put(2, 3);

        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        double balanceDebitCard = 100.0;

        Calculator calculator = new Calculator(mockWriter);
        List<PaySlip> paySlips = calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard, null);

        assertEquals(2, paySlips.size());

        PaySlip paySlip1 = paySlips.get(0);
        assertEquals("Product A", paySlip1.getDescription());
        assertEquals(2, paySlip1.getQuantity());
        assertEquals(decimalFormat.format(10.0), decimalFormat.format(paySlip1.getPrice()));
        assertEquals(decimalFormat.format(19.40), decimalFormat.format(paySlip1.getTotal()));
        assertEquals(decimalFormat.format(0.60), decimalFormat.format(paySlip1.getDiscount()));

        PaySlip paySlip2 = paySlips.get(1);
        assertEquals("Product B", paySlip2.getDescription());
        assertEquals(3, paySlip2.getQuantity());
        assertEquals(decimalFormat.format(20.00), decimalFormat.format(paySlip2.getPrice()));
        assertEquals(decimalFormat.format(58.20), decimalFormat.format(paySlip2.getTotal()));
        assertEquals(decimalFormat.format(1.80), decimalFormat.format(paySlip2.getDiscount()));
    }

    @Test
    void testQuantityExceedsStock() {
        List<Product> products = List.of(
                new Product(1, "Product A", 10.0, 10, false)
        );
        Map<Integer, Integer> purchases = new HashMap<>();
        purchases.put(1, 11);

        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        double balanceDebitCard = 100.0;

        Calculator calculator = new Calculator(mockWriter);


        assertThrows(RuntimeException.class, () ->
                calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard, null)
        );
    }

    @Test
    void testCombineSameProducts() throws NotEnoughMoneyException {
        List<Product> products = List.of(
                new Product(1, "Product A", 10.0, 10, false)
        );
        Map<Integer, Integer> purchases = new HashMap<>();
        purchases.put(1, 5);

        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        double balanceDebitCard = 100.0;

        Calculator calculator = new Calculator(mockWriter);
        List<PaySlip> paySlips = calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard, null);

        assertEquals(1, paySlips.size());

        PaySlip paySlip = paySlips.get(0);
        assertEquals("Product A", paySlip.getDescription());
        assertEquals(5, paySlip.getQuantity());
        assertEquals(10.0, paySlip.getPrice());
        assertEquals(48.5, paySlip.getTotal());
        assertEquals(1.5, paySlip.getDiscount());
    }

}
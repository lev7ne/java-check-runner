package ru.clevertec.check.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.dto.PaySlip;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CalculatorTest {

    private final Calculator calculator = new Calculator();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulPurchase() throws BadRequestException, NotEnoughMoneyException {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product A", 10.0, 10, false));
        products.add(new Product(2, "Product B", 20.0, 5, true));

        Map<Long, Integer> purchases = new HashMap<>();
        purchases.put(1L, 2);
        purchases.put(2L, 3);

        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        double balanceDebitCard = 100.0;

        List<PaySlip> paySlips = calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard);

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
        Map<Long, Integer> purchases = new HashMap<>();
        purchases.put(1L, 11);

        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        double balanceDebitCard = 100.0;

        assertThrows(BadRequestException.class, () ->
                calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard)
        );
    }

    @Test
    void testCombineSameProducts() throws BadRequestException, NotEnoughMoneyException {
        List<Product> products = List.of(
                new Product(1, "Product A", 10.0, 10, false)
        );
        Map<Long, Integer> purchases = new HashMap<>();
        purchases.put(1L, 5);

        DiscountCard discountCard = new DiscountCard(1, 1111, 3);
        double balanceDebitCard = 100.0;

        Calculator calculator = new Calculator();
        List<PaySlip> paySlips = calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard);

        assertEquals(1, paySlips.size());

        PaySlip paySlip = paySlips.get(0);
        assertEquals("Product A", paySlip.getDescription());
        assertEquals(5, paySlip.getQuantity());
        assertEquals(10.0, paySlip.getPrice());
        assertEquals(48.5, paySlip.getTotal());
        assertEquals(1.5, paySlip.getDiscount());
    }

    @Test
    void testPurchaseCalculate_NoDiscountCard() throws BadRequestException, NotEnoughMoneyException {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Product A", 10.0, 10, false));
        products.add(new Product(2, "Product B", 20.0, 5, true));

        Map<Long, Integer> purchases = new HashMap<>();
        purchases.put(1L, 2);
        purchases.put(2L, 3);

        DiscountCard discountCard = null;
        double balanceDebitCard = 100.0;

        List<PaySlip> paySlips = calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard);


        PaySlip paySlip1 = paySlips.get(0);
        assertEquals("Product A", paySlip1.getDescription());
        assertEquals(2, paySlip1.getQuantity());
        assertEquals(decimalFormat.format(10.0), decimalFormat.format(paySlip1.getPrice()));
        assertEquals(decimalFormat.format(20.00), decimalFormat.format(paySlip1.getTotal()));
        assertEquals(decimalFormat.format(0.00), decimalFormat.format(paySlip1.getDiscount()));

        PaySlip paySlip2 = paySlips.get(1);
        assertEquals("Product B", paySlip2.getDescription());
        assertEquals(3, paySlip2.getQuantity());
        assertEquals(decimalFormat.format(20.00), decimalFormat.format(paySlip2.getPrice()));
        assertEquals(decimalFormat.format(60.00), decimalFormat.format(paySlip2.getTotal()));
        assertEquals(decimalFormat.format(0.00), decimalFormat.format(paySlip2.getDiscount()));
    }

    @Test
    void testPurchaseCalculate_BadRequest() {
        Map<Long, Integer> purchases = new HashMap<>();
        purchases.put(1L, 3);
        purchases.put(2L, 15);

        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Product 1", 100.0, 10, false));
        products.add(new Product(2L, "Product 2", 50.0, 10, false));

        DiscountCard discountCard = null;

        double balanceDebitCard = 500.0;

        assertThrows(BadRequestException.class, () -> {
            calculator.purchaseCalculate(purchases, products, discountCard, balanceDebitCard);
        });
    }

}
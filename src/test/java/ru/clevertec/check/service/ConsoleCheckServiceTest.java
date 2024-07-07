package ru.clevertec.check.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.dao.DiscountCardDAO;
import ru.clevertec.check.dao.ProductDAO;
import ru.clevertec.check.dto.ArgsContext;
import ru.clevertec.check.dto.PaySlip;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.exception.NotEnoughMoneyException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.util.Calculator;
import ru.clevertec.check.util.Writer;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsoleCheckServiceTest {
    @Mock
    private Calculator mockCalculator;

    @Mock
    private Writer mockWriter;

    @Mock
    private ProductDAO mockProductDAO;

    @Mock
    private DiscountCardDAO mockDiscountCardDAO;

    private CheckService checkService;
    private ArgsContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, String> db = new HashMap<>();
        db.put("datasource.url", "jdbc:h2:mem:check");
        db.put("datasource.username", "ci");
        db.put("datasource.password", "test");

        Map<Integer, Integer> purchases = new HashMap<>();
        purchases.put(1, 3);

        context = new ArgsContext(purchases, 1111, 100.0, "result.csv", db);
        checkService = new ConsoleCheckService(mockWriter, mockCalculator, mockProductDAO, mockDiscountCardDAO, context);
    }

    @Test
    void testCreateCheck() throws BadRequestException, NotEnoughMoneyException {

        List<Product> products = Collections.singletonList(new Product(1, "Product A", 10.0, 10, false));

        DiscountCard discountCard = new DiscountCard(1, 1111, 3);

        List<PaySlip> paySlips = List.of(new PaySlip(3, "Product A", 10.0, 0.9, 29.1));

        when(mockCalculator.purchaseCalculate(context.getPurchases(), products, discountCard, context.getBalanceDebitCard(), context.getSaveToFile())).thenReturn(paySlips);
        when(mockProductDAO.findByIds(new ArrayList<>(context.getPurchases().keySet()))).thenReturn(products);
        when(mockDiscountCardDAO.findByNumber(context.getDiscountCard())).thenReturn(Optional.of(discountCard));

        checkService.createCheck();

        verify(mockCalculator).purchaseCalculate(context.getPurchases(), products, discountCard, context.getBalanceDebitCard(), context.getSaveToFile());
        verify(mockWriter).writeToCsv(paySlips, discountCard, context.getSaveToFile());
    }

}
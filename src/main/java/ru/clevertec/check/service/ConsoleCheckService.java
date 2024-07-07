package ru.clevertec.check.service;

import ru.clevertec.check.dao.DiscountCardDAO;
import ru.clevertec.check.dao.ProductDAO;
import ru.clevertec.check.dto.ArgsContext;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.util.Calculator;
import ru.clevertec.check.util.Writer;

import java.util.ArrayList;
import java.util.List;


public class ConsoleCheckService implements CheckService {
    private final ArgsContext context;
    private final Writer writer;
    private final Calculator calculator;
    private final ProductDAO productDAO;
    private final DiscountCardDAO discountCardDAO;

    public ConsoleCheckService(
            Writer writer,
            Calculator calculator,
            ProductDAO productDAO,
            DiscountCardDAO discountCardDAO,
            ArgsContext context
    ) {
        this.context = context;
        this.writer = writer;
        this.calculator = calculator;
        this.productDAO = productDAO;
        this.discountCardDAO = discountCardDAO;
    }

    @Override
    public void createCheck() {
        DiscountCard discountCard = new DiscountCard(0);

        var saveToFile = context.getSaveToFile();
        List<Product> products;

        List<Integer> ids = new ArrayList<>(context.getPurchases().keySet());
        products = productDAO.findByIds(ids);

        if (context.getDiscountCard() != null) {
            discountCard = discountCardDAO.findByNumber(context.getDiscountCard())
                    .orElse(discountCard);
        }

        var paySlips = calculator.purchaseCalculate(
                context.getPurchases(),
                products,
                discountCard,
                context.getBalanceDebitCard(),
                context.getSaveToFile()
        );

        writer.writeToCsv(paySlips, discountCard, saveToFile);
    }

}

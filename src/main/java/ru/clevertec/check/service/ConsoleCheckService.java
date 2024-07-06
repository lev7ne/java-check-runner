package main.java.ru.clevertec.check.service;

import main.java.ru.clevertec.check.csv.DiscountCardCSV;
import main.java.ru.clevertec.check.csv.ProductCSV;
import main.java.ru.clevertec.check.dto.ArgsContext;
import main.java.ru.clevertec.check.dto.PaySlip;
import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.exception.NotEnoughMoneyException;
import main.java.ru.clevertec.check.model.DiscountCard;
import main.java.ru.clevertec.check.model.Product;
import main.java.ru.clevertec.check.util.Calculator;
import main.java.ru.clevertec.check.util.Parser;
import main.java.ru.clevertec.check.util.Writer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class ConsoleCheckService implements CheckService {
    private static final Path DISCOUNT_CARDS_PATH = Path.of("./src/main/resources/discountCards.csv");

    @Override
    public void createCheck(String[] args) {
        String exception = null;
        ArgsContext context;
        List<PaySlip> paySlips = new ArrayList<>();
        DiscountCard discountCard = new DiscountCard(0);
        String saveToFile = null;

        try {
            context = Parser.parseArgs(args);
            saveToFile = context.getSaveToFile();

            List<Product> products;

            var productCSV = new ProductCSV(Path.of(context.getPathToFile()));
            products = productCSV.findByIds(context.getPurchases());

            if (context.getDiscountCardNumber() != null) {
                var discountCSV = new DiscountCardCSV(DISCOUNT_CARDS_PATH);
                discountCard = discountCSV.findByNumber(context.getDiscountCardNumber());
            }

            paySlips = Calculator.purchaseCalculate(
                    context.getPurchases(),
                    products,
                    discountCard,
                    context.getBalanceDebitCard()
            );

        } catch (BadRequestException | IOException e) {
            exception = e.getMessage();
            if (e instanceof BadRequestException) {
                saveToFile = ((BadRequestException) e).getDetails();
            }
        } catch (NotEnoughMoneyException e) {
            exception = e.getMessage();
        }

        Writer.writeToCsv(paySlips, discountCard, exception, saveToFile);
    }

}

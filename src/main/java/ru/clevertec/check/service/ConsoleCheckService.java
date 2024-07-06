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
    private static final Path PRODUCTS_PATH = Path.of("./src/main/resources/products.csv");
    private static final Path DISCOUNT_CARDS_PATH = Path.of("./src/main/resources/discountCards.csv");

    @Override
    public void createCheck(String[] args) {
        // Готовим пустые переменные, перед тем как начать читать информацию из аргументов
        String exception = "";
        var context = new ArgsContext();
        List<PaySlip> paySlips = new ArrayList<>();
        var discountCard = new DiscountCard();

        try {
            // Пытаемся спарсить аргументы
            context = Parser.parseArgs(args);

            var productCSV = new ProductCSV(PRODUCTS_PATH);
            List<Product> products = new ArrayList<>();

            // Если не упали в блок catch, значит можно пытаться достать данные из таблиц
            try {
                products = productCSV.findByIds(context.getPurchases());
            } catch (IOException e) {
                // Пишем лог в консоль, если есть проблемы с исходным файлом product.csv
                exception = e.getMessage();
                Writer.writeToCsv(paySlips, discountCard, exception);
            }

            if (context.getDiscountCard() != null) {
                var discountCSV = new DiscountCardCSV(DISCOUNT_CARDS_PATH);
                try {
                    discountCard = discountCSV.findByNumber(context.getDiscountCard());
                } catch (IOException e) {
                    // Пишем лог в консоль, если есть проблемы с исходным файлом discountCards.csv
                    exception = e.getMessage();
                    Writer.writeToCsv(paySlips, discountCard, exception);
                }
            } else {
                // Ставим флаг для дисконтной карты, если не передана в аргументах и не нужно писать инфо в чек
                discountCard.setId(0);
            }

            try {
                // Основной подсчет делаем в утилитарном сервисе
                paySlips = Calculator.purchaseCalculate(context.getPurchases(), products, discountCard, context.getBalanceDebitCard());
            } catch (NotEnoughMoneyException e) {
                // Если ловим исключение, готовим инфо для записи
                exception = e.getMessage();
            }

        } catch (BadRequestException e) {
            // Пишем лог в консоль, если есть проблемы с исходным файлом discountCards.csv
            System.out.printf("Ошибка чтения args[]: %s%n", e.getMessage());
            exception = e.getMessage();
        }

        Writer.writeToCsv(paySlips, discountCard, exception);
    }

}

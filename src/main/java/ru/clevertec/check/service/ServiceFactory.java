package ru.clevertec.check.service;

import ru.clevertec.check.dao.DiscountCardDAO;
import ru.clevertec.check.dao.ProductDAO;
import ru.clevertec.check.dto.ArgsContext;
import ru.clevertec.check.util.Calculator;
import ru.clevertec.check.util.Parser;
import ru.clevertec.check.util.Writer;


public class ServiceFactory {
    private final static Writer writer = new Writer();
    private final static Parser parser = new Parser(writer);
    private final static Calculator calculator = new Calculator(writer);

    public static CheckService getConsoleCheckService(String[] args) {
        ArgsContext context = parser.parseArgs(args);

        var db = context.getDatasource();
        var url = db.get("datasource.url");
        var username = db.get("datasource.username");
        var password = db.get("datasource.password");

        ProductDAO productDAO = new ProductDAO(url, username, password);
        DiscountCardDAO discountCardDAO = new DiscountCardDAO(url, username, password);


        return new ConsoleCheckService(writer, calculator, productDAO, discountCardDAO, context);
    }
}

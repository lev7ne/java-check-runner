package main.java.ru.clevertec.check;

import main.java.ru.clevertec.check.service.CheckService;
import main.java.ru.clevertec.check.service.ServiceFactory;


public class CheckRunner {
    public static void main(String[] args) {

        CheckService consoleCheckService = ServiceFactory.getConsoleCheckService();
        consoleCheckService.createCheck(args);

    }

}
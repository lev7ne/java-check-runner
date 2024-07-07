package ru.clevertec.check;


import ru.clevertec.check.service.CheckService;
import ru.clevertec.check.service.ServiceFactory;


public class CheckRunner {
    public static void main(String[] args) {

        CheckService consoleCheckService = ServiceFactory.getConsoleCheckService(args);
        consoleCheckService.createCheck();

    }

}
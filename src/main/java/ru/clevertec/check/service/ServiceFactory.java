package main.java.ru.clevertec.check.service;

public class ServiceFactory {
    public static CheckService getConsoleCheckService() {
        return new ConsoleCheckService();
    }
}

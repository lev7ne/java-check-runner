package main.java.ru.clevertec.check.util;

import main.java.ru.clevertec.check.dto.ArgsContext;
import main.java.ru.clevertec.check.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;


public class Parser {

    /**
     * Утилитарный метод для парсинга аргументов запуска
     *
     * @param args - переданные при запуске аргументы
     * @return ArgsContext агрегированные в Java-объект данные, пришедшие в аргументах запуска
     * @throws BadRequestException - метод бросает исключение с сообщением "BAD REQUEST" для записи в чек
     */
    public static ArgsContext parseArgs(String[] args) throws BadRequestException {
        if (args.length == 0) {
            System.out.println("Не переданы аргументы");
            throw new BadRequestException("BAD REQUEST");
        }

        Map<Integer, Integer> purchases = new HashMap<>();
        Integer discountCard = null;
        Double balanceDebitCard = null;

        for (String arg : args) {
            if (arg.contains("=")) {
                String[] parts = arg.split("=");
                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "discountCard":
                        if (value.matches("\\d{4}")) {
                            discountCard = Integer.parseInt(value);
                        } else {
                            System.out.println("Некорректный формат discountCard, ожидается четыре цифры");
                            throw new BadRequestException("BAD REQUEST");
                        }
                        break;
                    case "balanceDebitCard":
                        try {
                            balanceDebitCard = Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            System.out.println("Некорректный формат balanceDebitCard, ожидается число");
                            throw new BadRequestException("BAD REQUEST");
                        }
                        break;
                    default:
                        System.out.println("Передан неизвестный параметр");
                        throw new BadRequestException("BAD REQUEST");
                }
            } else if (arg.contains("-")) {
                String[] parts = arg.split("-", 2);
                try {
                    int productId = Integer.parseInt(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);
                    purchases.merge(productId, quantity, Integer::sum);

                } catch (NumberFormatException e) {
                    System.out.println("Некорректный формат id-quantity, ожидаются целые числа");
                    throw new BadRequestException("BAD REQUEST");
                }
            } else {
                System.out.println("Передан неизвестный параметр");
                throw new BadRequestException("BAD REQUEST");
            }
        }

        if (purchases.isEmpty()) {
            System.out.println("Необходимо передать минимум одну связку id-quantity");
            throw new BadRequestException("BAD REQUEST");
        }

        if (balanceDebitCard == null) {
            System.out.println("Аргумент для balanceDebitCard обязателен");
            throw new BadRequestException("BAD REQUEST");
        }

        return new ArgsContext(purchases, discountCard, balanceDebitCard);
    }

}


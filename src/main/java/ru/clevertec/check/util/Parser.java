package ru.clevertec.check.util;

import ru.clevertec.check.dto.ArgsContext;
import ru.clevertec.check.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;


public class Parser {
    private final Writer writer;

    public Parser(Writer writer) {
        this.writer = writer;
    }

    /**
     * Утилитарный метод для парсинга аргументов запуска
     *
     * @param args - переданные при запуске аргументы
     * @return ArgsContext агрегированные в Java-объект данные, пришедшие в аргументах запуска
     * @throws RuntimeException - метод бросает исключение завершая программу и печатает информацию об ошибке
     */
    public ArgsContext parseArgs(String[] args) throws BadRequestException {
        try {
            if (args.length == 0) {
                System.out.println("Не переданы аргументы");
                throw new BadRequestException("BAD REQUEST");
            }

            Map<Integer, Integer> purchases = new HashMap<>();
            Integer discountCard = null;
            Double balanceDebitCard = null;
            String saveToFile = null;
            Map<String, String> database = new HashMap<>();

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
                                throw new BadRequestException("BAD REQUEST", saveToFile);
                            }
                            break;
                        case "balanceDebitCard":
                            try {
                                balanceDebitCard = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                                System.out.println("Некорректный формат balanceDebitCard, ожидается число");
                                throw new BadRequestException("BAD REQUEST", saveToFile);
                            }
                            break;
                        case "saveToFile":
                            saveToFile = value;
                            break;
                        case "datasource.url":
                        case "datasource.password":
                        case "datasource.username":
                            database.put(key, value);
                            break;
                        default:
                            System.out.println("Передан неизвестный параметр");
                            throw new BadRequestException("BAD REQUEST", saveToFile);
                    }

                } else if (arg.contains("-")) {
                    String[] parts = arg.split("-", 2);
                    try {
                        int productId = Integer.parseInt(parts[0]);
                        int quantity = Integer.parseInt(parts[1]);
                        purchases.merge(productId, quantity, Integer::sum);

                    } catch (NumberFormatException e) {
                        System.out.println("Некорректный формат id-quantity, ожидаются целые числа");
                        throw new BadRequestException("BAD REQUEST", saveToFile);
                    }

                } else {
                    System.out.println("Передан неизвестный параметр");
                    throw new BadRequestException("BAD REQUEST", saveToFile);
                }
            }

            if (purchases.isEmpty()) {
                System.out.println("Необходимо передать минимум одну связку id-quantity");
                throw new BadRequestException("BAD REQUEST", saveToFile);
            }

            if (balanceDebitCard == null) {
                System.out.println("Аргумент для balanceDebitCard обязателен");
                throw new BadRequestException("BAD REQUEST", saveToFile);
            }

            return new ArgsContext(purchases, discountCard, balanceDebitCard, saveToFile, database);

        } catch (BadRequestException e) {
            writer.writeError(e.getMessage(), e.getPath());
            System.out.printf("Возникла ошибка: %s%n", e.getMessage());
            throw new RuntimeException();
        }
    }

}


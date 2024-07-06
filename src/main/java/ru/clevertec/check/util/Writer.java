package main.java.ru.clevertec.check.util;

import main.java.ru.clevertec.check.dto.PaySlip;
import main.java.ru.clevertec.check.model.DiscountCard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Writer {

    /**
     * Утилитарный метод для записи результатов в .csv
     *
     * @param paySlips     - список покупок, сгруппированный для записи в чек
     * @param discountCard - дисконтная карта
     * @param exception    - сообщение об ошибке, может прийти пустым
     */
    public static void writeToCsv(List<PaySlip> paySlips, DiscountCard discountCard, String exception) {
        String filePath = "result.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Если при сборе данных возникла ошибка, пишем
            if (!exception.isEmpty()) {
                writer.write("ERROR\n");
                writer.write(exception + "\n");
                return;
            }

            // Запись текущего времени
            var now = LocalDateTime.now();
            writer.write("Date;Time\n");
            writer.write(now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + ";");
            writer.write(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "\n\n");

            // Запись строк чека
            writer.write("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
            double totalPrice = 0.00;
            double totalDiscount = 0.00;
            for (PaySlip line : paySlips) {
                totalPrice += line.getTotal();
                totalDiscount += line.getDiscount();
                writer.write(line.getQuantity() + ";");
                writer.write(line.getDescription() + ";");
                writer.write(String.format("%.2f$", line.getPrice()) + ";");
                writer.write(String.format("%.2f$", line.getDiscount()) + ";");
                writer.write(String.format("%.2f$", line.getTotal()) + "\n");
            }
            writer.write("\n");

            // Запись информации о дисконтной карте, если есть
            if (discountCard.getNumberDiscount() != 0) {
                writer.write("DISCOUNT CARD;DISCOUNT PERCENTAGE\n");
                writer.write(discountCard.getNumberDiscount() + ";");
                writer.write(discountCard.getAmount() + "%\n\n");
            }

            // Запись итоговых сумм
            writer.write("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            writer.write(String.format("%.2f$", totalPrice) + ";");
            writer.write(String.format("%.2f$", totalDiscount) + ";");
            writer.write(String.format("%.2f$", (totalPrice - totalDiscount)) + "\n");

        } catch (IOException e) {
            System.out.printf("Во время записи чека возникла ошибка: %s%n", e.getMessage());
        }

        System.out.printf("Чек создан: %s", filePath);
    }
}

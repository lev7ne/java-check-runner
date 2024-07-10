package ru.clevertec.check.util;

import ru.clevertec.check.dto.PaySlip;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.model.DiscountCard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class Writer {
    private static final String FILE_PATH = "result.csv";

    public static File writeToFile(List<PaySlip> paySlips, DiscountCard discountCard) throws InternalServerException {
        if (paySlips == null || paySlips.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {

            var now = LocalDateTime.now();
            sb.append("Date;Time\n");
            sb.append(now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))).append(";");
            sb.append(now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n\n");

            sb.append("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL\n");
            double totalPrice = 0.00;
            double totalDiscount = 0.00;
            for (PaySlip line : paySlips) {
                totalPrice += line.getTotal();
                totalDiscount += line.getDiscount();
                sb.append(line.getQuantity()).append(";");
                sb.append(line.getDescription()).append(";");
                sb.append(String.format("%.2f$", line.getPrice())).append(";");
                sb.append(String.format("%.2f$", line.getDiscount())).append(";");
                sb.append(String.format("%.2f$", line.getTotal())).append("\n");
            }
            sb.append("\n");

            if (discountCard != null) {
                sb.append("DISCOUNT CARD;DISCOUNT PERCENTAGE\n");
                sb.append(discountCard.getDiscountCard()).append(";");
                sb.append(discountCard.getDiscountAmount()).append("%\n\n");
            }

            sb.append("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT\n");
            sb.append(String.format("%.2f$", totalPrice)).append(";");
            sb.append(String.format("%.2f$", totalDiscount)).append(";");
            sb.append(String.format("%.2f$", (totalPrice - totalDiscount))).append("\n");

            writer.write(sb.toString());
        } catch (IOException e) {
            throw new InternalServerException("INTERNAL SERVER ERROR");
        }

        return new File(FILE_PATH);
    }

}

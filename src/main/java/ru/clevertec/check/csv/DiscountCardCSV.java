package main.java.ru.clevertec.check.csv;

import main.java.ru.clevertec.check.model.DiscountCard;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class DiscountCardCSV {
    private final Path discountCardsPath;

    public DiscountCardCSV(Path discountCardsPath) {
        this.discountCardsPath = discountCardsPath;
    }

    public DiscountCard findByNumber(int number) throws IOException {
        DiscountCard discountCard = new DiscountCard(0, 0, 0);

        try (BufferedReader reader = Files.newBufferedReader(discountCardsPath)) {
            // Пропуск заголовка
            String line = reader.readLine();
            if (line == null) {
                System.out.printf("Ошибка при чтении файла: %s%n", discountCardsPath);
                throw new IOException("EXTERNAL SERVER ERROR");
            }

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(";");

                if (columns.length < 3) continue;

                int numberDiscount = Integer.parseInt(columns[1]);

                if (numberDiscount == number) {
                    int id = Integer.parseInt(columns[0]);
                    int amount = Integer.parseInt(columns[2]);

                    discountCard.setId(id);
                    discountCard.setNumberDiscount(numberDiscount);
                    discountCard.setAmount(amount);
                    return discountCard;
                }
            }

            // Если не найден, устанавливаем номер и дефолтную скидку
            discountCard.setNumberDiscount(number);
            discountCard.setAmount(2);
            return discountCard;
        }
    }
}

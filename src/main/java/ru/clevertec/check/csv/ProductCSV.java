package main.java.ru.clevertec.check.csv;

import main.java.ru.clevertec.check.exception.BadRequestException;
import main.java.ru.clevertec.check.model.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;


public class ProductCSV {
    private final Path productsPath;

    public ProductCSV(Path productsPath) {
        this.productsPath = productsPath;
    }

    public List<Product> findByIds(Map<Integer, Integer> purchases) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(productsPath)) {
            String line = reader.readLine();
            if (line == null) {
                System.out.printf("Ошибка при чтении файла: %s%n", productsPath);
                throw new IOException("EXTERNAL SERVER ERROR");
            }

            List<Product> products = reader.lines()
                    .map(lineContent -> lineContent.split(";"))
                    .map(columns -> new Product(
                            Integer.parseInt(columns[0]), // id
                            columns[1],                   // название
                            Double.parseDouble(columns[2].replace(",", ".")), // цена
                            Integer.parseInt(columns[3]), // количество на складе
                            columns[4].equals("+")        // скидка доступна
                    ))
                    .filter(product -> purchases.containsKey(product.getId()))
                    .peek(product -> {
                        int requestedQuantity = purchases.get(product.getId());
                        if (requestedQuantity > product.getQuantityInStock()) {
                            System.out.printf("Запрашиваемого товара с id: %s, нет на складе в количестве: %s%n",
                                    product.getId(), product.getQuantityInStock());
                            throw new BadRequestException("BAD REQUEST");
                        }
                    })
                    .toList();

            if (products.size() != purchases.size()) {
                System.out.println("Не все, из запрашиваемых товаров найдены");
                throw new BadRequestException("BAD REQUEST");
            }

            return products;

        } catch (NoSuchFileException e) {
            System.out.println("По указанному пути файл не найден");
            throw new BadRequestException("BAD REQUEST");
        }
    }
}

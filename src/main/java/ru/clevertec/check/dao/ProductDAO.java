package ru.clevertec.check.dao;

import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.model.Product;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final String url;
    private final String username;
    private final String password;

    public ProductDAO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public List<Product> findByIds(List<Integer> ids) throws BadRequestException {
        List<Product> products = new ArrayList<>();

        if (ids.isEmpty()) {
            return products;
        }

        var sql = new StringBuilder(
                "SELECT id, description, price, quantity_in_stock, wholesale_product " +
                        "FROM product " +
                        "WHERE id IN ("
        );

        for (int i = 0; i < ids.size(); i++) {
            sql.append("?");
            if (i < ids.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");

        try (var conn = DriverManager.getConnection(url, username, password)) {
            try (var pstmt = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < ids.size(); i++) {
                    pstmt.setInt(i + 1, ids.get(i));
                }

                try (var rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Product product = new Product();
                        product.setId(rs.getInt("id"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getDouble("price"));
                        product.setQuantityInStock(rs.getInt("quantity_in_stock"));
                        product.setWholesaleProduct(rs.getBoolean("wholesale_product"));
                        products.add(product);
                    }
                }

                return products;

            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении информации из базы данных");
            throw new BadRequestException("EXTERNAL SERVER ERROR");
        }
    }

}

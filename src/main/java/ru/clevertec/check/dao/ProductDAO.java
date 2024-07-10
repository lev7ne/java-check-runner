package ru.clevertec.check.dao;

import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO {
    private final Connection conn;

    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public Product findById(long id) throws InternalServerException, NotFoundException {
        String sql = "SELECT * FROM product WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            var resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity_in_stock"),
                        resultSet.getBoolean("wholesale_product")
                );
            } else {
                throw new NotFoundException("Продукт с id: " + id + " не найден");
            }
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public void addProduct(Product product) throws InternalServerException {
        String sql = "INSERT INTO product (description, price, quantity_in_stock, wholesale_product) VALUES (?, ?, ?, ?)";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getDescription());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getQuantity());
            pstmt.setBoolean(4, product.isWholesale());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public void updateProduct(long id, Product product) throws InternalServerException, NotFoundException {
        String sql = "UPDATE product SET description = ?, price = ?, quantity_in_stock = ?, wholesale_product = ? WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, product.getDescription());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getQuantity());
            pstmt.setBoolean(4, product.isWholesale());
            pstmt.setLong(5, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Продукт для обновления с id: " + id + " не найден");
            }
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public void deleteProduct(long id) throws InternalServerException, NotFoundException {
        String sql = "DELETE FROM product WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Продукт для удаления с id: " + id + " не найден");
            }
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public List<Product> findByIds(List<Long> ids) throws InternalServerException {
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

        try (var pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < ids.size(); i++) {
                pstmt.setLong(i + 1, ids.get(i));
            }

            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getLong("id"),
                            rs.getString("description"),
                            rs.getDouble("price"),
                            rs.getInt("quantity_in_stock"),
                            rs.getBoolean("wholesale_product")
                    );
                    products.add(product);
                }
                if (products.isEmpty()) {
                    return List.of();
                }
            }
            return products;

        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }
}

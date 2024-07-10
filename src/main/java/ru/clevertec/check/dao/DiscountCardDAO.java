package ru.clevertec.check.dao;

import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DiscountCardDAO {
    private final Connection conn;

    public DiscountCardDAO(Connection conn) {
        this.conn = conn;
    }

    public DiscountCard findById(long id) throws InternalServerException, NotFoundException {
        String sql = "SELECT * FROM discount_card WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return new DiscountCard(
                        resultSet.getLong("id"),
                        resultSet.getInt("number_discount"),
                        resultSet.getInt("amount")
                );
            } else {
                throw new NotFoundException("Дисконтная карта id: " + id + " не найдена");
            }
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public DiscountCard findByNumber(int number) throws InternalServerException, NotFoundException {
        var sql = "SELECT * FROM discount_card WHERE number_discount = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, number);
            var resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getInt("id");
                var numberDiscount = resultSet.getInt("number_discount");
                var amount = resultSet.getInt("amount");
                return new DiscountCard(id, numberDiscount, amount);
            } else {
                throw new NotFoundException("Дисконтная карта с номером: " + number + " не найдена");
            }
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public DiscountCard addDiscountCard(DiscountCard card) throws InternalServerException {
        String sql = "INSERT INTO discount_card (number_discount, amount) VALUES (?, ?)";
        try (var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, card.getDiscountCard());
            pstmt.setInt(2, card.getDiscountAmount());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new InternalServerException("Не удалось создать дисконтную карту");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    card.setId(generatedKeys.getLong("id"));
                } else {
                    throw new InternalServerException("Не удалось создать дисконтную карту, идентификатор не получен");
                }
            }

            return card;
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public void updateDiscountCard(long id, DiscountCard card) throws InternalServerException, NotFoundException {
        String sql = "UPDATE discount_card SET number_discount = ?, amount = ? WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, card.getDiscountCard());
            pstmt.setInt(2, card.getDiscountAmount());
            pstmt.setLong(3, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Дисконтная карта для обновления с id: " + id + " не найдена");
            }
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public void deleteDiscountCard(long id) throws InternalServerException, NotFoundException {
        String sql = "DELETE FROM discount_card WHERE id = ?";
        try (var pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new NotFoundException("Дисконтная карта для удаления с id: " + id + " не найдена");
            }
        } catch (SQLException e) {
            throw new InternalServerException("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

}
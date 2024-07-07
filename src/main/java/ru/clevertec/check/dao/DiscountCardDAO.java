package ru.clevertec.check.dao;

import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.model.DiscountCard;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

public class DiscountCardDAO {
    private final String url;
    private final String username;
    private final String password;

    public DiscountCardDAO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Optional<DiscountCard> findByNumber(int number) throws BadRequestException {
        var sql = "SELECT * FROM discount_card WHERE number_discount = ?";
        
        try (var conn = DriverManager.getConnection(url, username, password)) {

            try (var pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, number);
                var resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    var id = resultSet.getInt("id");
                    var numberDiscount = resultSet.getInt("number_discount");
                    var amount = resultSet.getInt("amount");
                    var discountCard = new DiscountCard(id, numberDiscount, amount);

                    return Optional.of(discountCard);
                }

                return Optional.empty();
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении информации из базы данных" + e.getMessage());
            throw new BadRequestException("EXTERNAL SERVER ERROR");
        }
    }

}
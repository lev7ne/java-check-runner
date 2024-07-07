package ru.clevertec.check.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.model.DiscountCard;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class DiscountCardDAOTest {

    private DiscountCardDAO discountCardDAO;

    private static final String TEST_URL = "jdbc:h2:~/test";
    private static final String TEST_USERNAME = "sa";
    private static final String TEST_PASSWORD = "";

    @BeforeAll
    public static void setUpDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
             Statement stmt = conn.createStatement()) {

            ClassLoader classLoader = DiscountCardDAOTest.class.getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource("data.sql")).getFile());

            String sqlScript = file.getAbsolutePath();
            stmt.execute("RUNSCRIPT FROM '" + sqlScript + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void clearDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS product");
            stmt.execute("DROP TABLE IF EXISTS discount_card");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        discountCardDAO = new DiscountCardDAO(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
    }

    @Test
    void testFindByNumberCardFound() {
        int number = 1111;

        Optional<DiscountCard> result = discountCardDAO.findByNumber(number);

        assertTrue(result.isPresent());
        assertEquals(number, result.get().getNumberDiscount());
        assertEquals(3, result.get().getAmount());
    }

    @Test
    void testFindByNumberCardNotFound() {
        int number = 54321;

        Optional<DiscountCard> result = discountCardDAO.findByNumber(number);

        assertFalse(result.isPresent());
    }

}
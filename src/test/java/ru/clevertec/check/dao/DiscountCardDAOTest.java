package ru.clevertec.check.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.DiscountCard;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class DiscountCardDAOTest {
    private static final String TEST_URL = "jdbc:h2:~/test;MODE=PostgreSQL";
    private static final String TEST_USERNAME = "sa";
    private static final String TEST_PASSWORD = "";
    private static DiscountCardDAO discountCardDAO;

    @BeforeAll
    public static void setUpDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
        discountCardDAO = new DiscountCardDAO(conn);

        try (Statement stmt = conn.createStatement()) {

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
    }

    @Test
    void testFindByNumberCardFound() throws InternalServerException, NotFoundException {
        int number = 1111;

        DiscountCard result = discountCardDAO.findByNumber(number);

        assertNotNull(result);
        assertEquals(number, result.getDiscountCard());
        assertEquals(3, result.getDiscountAmount());
    }

    @Test
    void testFindByNumberCardNotFound() {
        int number = 54321;

        assertThrows(NotFoundException.class, () -> discountCardDAO.findByNumber(number));
    }

    @Test
    void testFindByIdCardFound() throws InternalServerException, NotFoundException {
        int id = 1;

        DiscountCard result = discountCardDAO.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(1111, result.getDiscountCard());
        assertEquals(3, result.getDiscountAmount());
    }

    @Test
    void testFindByIdCardNotFound() {
        int id = 54321;

        assertThrows(NotFoundException.class, () -> discountCardDAO.findById(id));
    }

    @Test
    void testUpdateDiscountCard() throws InternalServerException, NotFoundException {
        int id = 2;
        DiscountCard updatedCard = new DiscountCard(id, 2222, 10);

        discountCardDAO.updateDiscountCard(id, updatedCard);

        DiscountCard result = discountCardDAO.findById(id);
        assertNotNull(result);
        assertEquals(2222, result.getDiscountCard());
        assertEquals(10, result.getDiscountAmount());
    }

    @Test
    void testDeleteDiscountCard() throws InternalServerException, NotFoundException {
        int id = 2;

        discountCardDAO.deleteDiscountCard(id);

        assertThrows(NotFoundException.class, () -> discountCardDAO.findById(id));
    }

    @Test
    void testDeleteDiscountCardNotFound() {
        int id = 54321;

        assertThrows(NotFoundException.class, () -> discountCardDAO.deleteDiscountCard(id));
    }

}
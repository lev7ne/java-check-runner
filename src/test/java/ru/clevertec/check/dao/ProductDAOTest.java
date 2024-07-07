package ru.clevertec.check.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.exception.BadRequestException;
import ru.clevertec.check.model.Product;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {
    private ProductDAO productDAO;

    private static final String TEST_URL = "jdbc:h2:~/test";
    private static final String TEST_USERNAME = "sa";
    private static final String TEST_PASSWORD = "";

    @BeforeAll
    public static void setUpDatabase() {
        try (Connection conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
             Statement stmt = conn.createStatement()) {

            ClassLoader classLoader = ProductDAOTest.class.getClassLoader();
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
        productDAO = new ProductDAO(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
    }

    @Test
    void testFindByIdsProductsFound() throws BadRequestException {
        List<Integer> ids = List.of(1, 2);

        List<Product> products = productDAO.findByIds(ids);

        assertEquals(2, products.size());

        Product product1 = products.get(0);
        assertEquals(1, product1.getId());
        assertEquals("Milk", product1.getDescription());
        assertEquals(10, product1.getQuantityInStock());
        assertTrue(product1.isWholesaleProduct());

        Product product2 = products.get(1);
        assertEquals(2, product2.getId());
        assertEquals("Cream 400g", product2.getDescription());
        assertEquals(20, product2.getQuantityInStock());
        assertTrue(product2.isWholesaleProduct());
    }

    @Test
    void testFindByIdsNoProductsFound() throws BadRequestException {
        List<Integer> ids = Arrays.asList(9999, 8888);

        List<Product> products = productDAO.findByIds(ids);

        assertTrue(products.isEmpty());
    }

    @Test
    void testFindByIdsEmptyList() throws BadRequestException {
        List<Integer> ids = new ArrayList<>();

        List<Product> products = productDAO.findByIds(ids);

        assertTrue(products.isEmpty());
    }

}
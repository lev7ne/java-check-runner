package ru.clevertec.check.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import ru.clevertec.check.exception.InternalServerException;
import ru.clevertec.check.exception.NotFoundException;
import ru.clevertec.check.model.Product;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest {
    private static final String TEST_URL = "jdbc:h2:~/test";
    private static final String TEST_USERNAME = "sa";
    private static final String TEST_PASSWORD = "";
    private static ProductDAO productDAO;

    @BeforeAll
    public static void setUpDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection(TEST_URL, TEST_USERNAME, TEST_PASSWORD);
        productDAO = new ProductDAO(conn);
        try (Statement stmt = conn.createStatement()) {

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
    }

    @Test
    void testFindByIdsProductsFound() throws InternalServerException, NotFoundException {
        List<Long> ids = List.of(1L, 2L);

        List<Product> products = productDAO.findByIds(ids);

        assertEquals(2, products.size());

        Product product1 = products.get(0);
        assertEquals(1, product1.getId());
        assertEquals("Milk", product1.getDescription());
        assertEquals(10, product1.getQuantity());
        assertTrue(product1.isWholesale());

        Product product2 = products.get(1);
        assertEquals(2, product2.getId());
        assertEquals("Cream 400g", product2.getDescription());
        assertEquals(20, product2.getQuantity());
        assertTrue(product2.isWholesale());
    }

    @Test
    void testFindByIdSuccess() throws Exception {
        Product product = productDAO.findById(1L);

        assertEquals(1, product.getId());
        assertEquals("Milk", product.getDescription());
        assertEquals(10, product.getQuantity());
        assertTrue(product.isWholesale());
    }

    @Test
    void testFindByIdsEmptyList() throws InternalServerException, NotFoundException {
        List<Long> ids = new ArrayList<>();

        List<Product> products = productDAO.findByIds(ids);

        assertTrue(products.isEmpty());
    }

    @Test
    void testUpdateProduct() throws InternalServerException, NotFoundException {
        long productId = 1L;

        Product originalProduct = productDAO.findById(productId);
        originalProduct.setDescription("Updated Milk");

        productDAO.updateProduct(productId, originalProduct);

        Product retrievedProduct = productDAO.findById(productId);
        assertEquals("Updated Milk", retrievedProduct.getDescription());
    }

    @Test
    void testUpdateProductNotFound() {
        var id = 1111L;
        Product product = new Product(id, "Updated Description", 20.0, 200, false);

        assertThrows(NotFoundException.class, () -> productDAO.updateProduct(id, product));
    }

    @Test
    void testDeleteProduct() throws InternalServerException, NotFoundException {
        long productId = 2L;

        productDAO.deleteProduct(productId);

        assertThrows(NotFoundException.class, () -> productDAO.findById(productId));
    }

}
package JUnitTest;

import catalogue.Product;
import dbAccess.StockRW;
import middle.StockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StockRW class.
 */
public class StockRWTest {

    private StockRW stockRW;
    private Connection connection;

    @BeforeEach
    void setup() throws Exception {
        // Initialize an in-memory SQLite database for testing
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        stockRW = new StockRW() {

            protected Connection getConnection() {
                return connection;
            }
        };

        // Create tables and insert test data
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE ProductTable (productNo TEXT PRIMARY KEY, description TEXT, price REAL, picture TEXT);");
            stmt.execute("CREATE TABLE StockTable (productNo TEXT PRIMARY KEY, stockLevel INTEGER, FOREIGN KEY (productNo) REFERENCES ProductTable(productNo));");

            stmt.execute("INSERT INTO ProductTable VALUES ('0001', 'Test Product 1', 10.0, 'image1.jpg');");
            stmt.execute("INSERT INTO StockTable VALUES ('0001', 100);");
        }
    }

    @Test
    void testGetDetails() throws StockException {
        Product product = stockRW.getDetails("0001");
        assertNotNull(product);
        assertEquals("0001", product.getProductNum());
        assertEquals("Test Product 1", product.getDescription());
        assertEquals(10.0, product.getPrice());
        assertEquals(100, product.getQuantity());
    }

    @Test
    void testBuyStock() throws StockException {
        // Buy 10 units of stock
        boolean result = stockRW.buyStock("0001", 10);
        assertTrue(result);

        // Check updated stock level
        Product product = stockRW.getDetails("0001");
        assertEquals(90, product.getQuantity());

        // Try to buy more stock than available
        result = stockRW.buyStock("0001", 200);
        assertFalse(result);
    }

    @Test
    void testAddStock() throws StockException {
        // Add 50 units of stock
        stockRW.addStock("0001", 50);

        // Check updated stock level
        Product product = stockRW.getDetails("0001");
        assertEquals(150, product.getQuantity());
    }

    @Test
    void testModifyStock() throws StockException {
        // Modify stock details
        Product newProduct = new Product("0001", "Updated Product", 20.0, 50, "updatedImage.jpg");
        stockRW.modifyStock(newProduct);

        // Check updated product details
        Product updatedProduct = stockRW.getDetails("0001");
        assertEquals("Updated Product", updatedProduct.getDescription());
        assertEquals(20.0, updatedProduct.getPrice());
        assertEquals(50, updatedProduct.getQuantity());
    }

    @Test
    void testGetAllProducts() throws StockException, SQLException {
        // Add another product
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO ProductTable VALUES ('0002', 'Test Product 2', 15.0, 'image2.jpg');");
            stmt.execute("INSERT INTO StockTable VALUES ('0002', 50);");
        }

        // Get all products
        List<Product> products = stockRW.getAllProducts();
        assertEquals(2, products.size());
    }
}

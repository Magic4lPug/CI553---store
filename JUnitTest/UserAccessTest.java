package JUnitTest;

import dbAccess.UserAccess;
import middle.StockException;
import org.junit.jupiter.api.*;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAccessTest {
    private Connection connection;
    private UserAccess userAccess;

    @BeforeAll
    void setupDatabase() throws Exception {
        // Set up SQLite in-memory database for testing
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite::memory:");
        connection = dataSource.getConnection();

        try (Statement stmt = connection.createStatement()) {
            // Create the UserTable
            stmt.executeUpdate("""
                CREATE TABLE UserTable (
                    userID TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    password TEXT NOT NULL,
                    salt TEXT NOT NULL,
                    email TEXT NOT NULL,
                    role TEXT
                );
            """);

            // Insert test user
            stmt.executeUpdate("""
                INSERT INTO UserTable (userID, username, password, salt, email, role)
                VALUES ('user1', 'testuser', 'hashedpassword', 'testsalt', 'testuser@example.com', 'customer');
            """);
        }

        userAccess = new UserAccess(connection);
    }

    @Test
    void testAuthenticateSuccess() throws StockException {
        boolean isAuthenticated = userAccess.authenticate("testuser", "hashedpassword");
        assertTrue(isAuthenticated, "User should be authenticated with correct credentials.");
    }

    @Test
    void testAuthenticateFail() throws StockException {
        boolean isAuthenticated = userAccess.authenticate("testuser", "wrongpassword");
        assertFalse(isAuthenticated, "User should not be authenticated with incorrect credentials.");
    }

    @Test
    void testDoesUsernameExist() throws StockException {
        assertTrue(userAccess.doesUsernameExist("testuser"), "Username should exist in the database.");
        assertFalse(userAccess.doesUsernameExist("nonexistentuser"), "Username should not exist in the database.");
    }

    @Test
    void testDoesEmailExist() throws StockException {
        assertTrue(userAccess.doesEmailExist("testuser@example.com"), "Email should exist in the database.");
        assertFalse(userAccess.doesEmailExist("nonexistent@example.com"), "Email should not exist in the database.");
    }

    @AfterAll
    void tearDownDatabase() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}

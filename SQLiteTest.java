import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteTest {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:test.db"; // SQLite database file

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("SQLite connected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}

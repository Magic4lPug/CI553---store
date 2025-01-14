package dbAccess;

import middle.StockException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccess {
    private final Connection theCon;

    public UserAccess(Connection connection) {
        this.theCon = connection;
    }

    public boolean authenticate(String username, String password) throws StockException {
        String query = "SELECT 1 FROM UserTable WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new StockException("SQL authenticate: " + e.getMessage());
        }
    }

    public boolean doesUsernameExist(String username) throws StockException {
        String query = "SELECT 1 FROM UserTable WHERE username = ?";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if a username exists
            }
        } catch (SQLException e) {
            throw new StockException("SQL doesUsernameExist: " + e.getMessage());
        }
    }

    public void createUser(String userID, String username, String password, String email, String role) throws StockException {
        String query = "INSERT INTO UserTable (userID, username, password, email, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, userID);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, email);
            stmt.setString(5, role);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StockException("SQL createUser: " + e.getMessage());
        }
    }

    public boolean authenticateWithEmail(String email, String password) throws StockException {
        String query = "SELECT 1 FROM UserTable WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) { // Use theCon instead of connection
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new StockException("SQL authenticateWithEmail: " + e.getMessage());
        }
    }

    public byte[] getBasketData(String userID) throws StockException {
        String query = "SELECT basketData FROM BasketTable WHERE userID = ?";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("basketData");
                }
            }
        } catch (SQLException e) {
            throw new StockException("SQL getBasketData: " + e.getMessage());
        }
        return null;
    }

    public void saveBasketData(String userID, byte[] basketData) throws StockException {
        String query = "INSERT OR REPLACE INTO BasketTable (userID, basketData) VALUES (?, ?)";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, userID);
            stmt.setBytes(2, basketData);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StockException("SQL saveBasketData: " + e.getMessage());
        }
    }

}

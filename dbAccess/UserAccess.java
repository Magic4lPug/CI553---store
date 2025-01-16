package dbAccess;

import middle.StockException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

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

    public boolean doesEmailExist(String email) throws StockException {
        String query = "SELECT 1 FROM UserTable WHERE email = ?";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if an email exists
            }
        } catch (SQLException e) {
            throw new StockException("SQL doesEmailExist: " + e.getMessage());
        }
    }

    public void createUser(String userID, String username, String password, String email, String role) throws StockException {
        String query = "INSERT INTO UserTable (userID, username, password, salt, email, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);

            stmt.setString(1, userID);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, Base64.getEncoder().encodeToString(salt)); // Store salt as Base64 string
            stmt.setString(5, email);
            stmt.setString(6, role);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new StockException("SQL createUser: " + e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new StockException("Error hashing password: " + e.getMessage());
        }
    }

    public String[] getCredentialsByLogin(String login) throws StockException {
        String query = login.contains("@")
                ? "SELECT salt, password FROM UserTable WHERE email = ?"
                : "SELECT salt, password FROM UserTable WHERE username = ?";

        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String salt = rs.getString("salt");
                    String hashedPassword = rs.getString("password");
                    return new String[]{salt, hashedPassword};
                }
            }
        } catch (SQLException e) {
            throw new StockException("SQL getCredentialsByLogin: " + e.getMessage());
        }
        return null; // Return null if no credentials are found
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

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashedBytes = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hashedBytes);
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

    public String getUserID(String login) throws StockException {
        String query = login.contains("@")
                ? "SELECT userID FROM UserTable WHERE email = ?"
                : "SELECT userID FROM UserTable WHERE username = ?";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("userID");
                }
            }
        } catch (SQLException e) {
            throw new StockException("SQL getUserID: " + e.getMessage());
        }
        return null; // Return null if no user found
    }

    public String getUserIDByLogin(String login, String password) throws StockException {
        String query = login.contains("@")
                ? "SELECT userID FROM UserTable WHERE email = ? AND password = ?"
                : "SELECT userID FROM UserTable WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, login);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("userID"); // Return the userID
                }
            }
        } catch (SQLException e) {
            throw new StockException("SQL getUserIDByLogin: " + e.getMessage());
        }
        return null; // Return null if no user matches
    }


    public String getUsername(String userID) throws StockException {
        String query = "SELECT username FROM UserTable WHERE userID = ?";
        try (PreparedStatement stmt = theCon.prepareStatement(query)) {
            stmt.setString(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new StockException("SQL getUsername: " + e.getMessage());
        }
        return null;
    }



}

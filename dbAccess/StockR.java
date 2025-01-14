package dbAccess;

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements read-only access to the stock database.
 */
public class StockR implements StockReader {
  protected Connection theCon = null; // Connection to database

  /**
   * Connects to the database using a factory method.
   *
   * @throws StockException if a problem occurs
   */
  public StockR() throws StockException {
    try {
      DBAccess dbDriver = (new DBAccessFactory()).getNewDBAccess();
      dbDriver.loadDriver();
      theCon = DriverManager.getConnection(
              dbDriver.urlOfDatabase(),
              dbDriver.username(),
              dbDriver.password()
      );
    } catch (SQLException e) {
      throw new StockException("SQL problem: " + e.getMessage());
    } catch (Exception e) {
      throw new StockException("Cannot load database driver.");
    }
  }

  @Override
  public synchronized boolean exists(String pNum) throws StockException {
    String query = "SELECT 1 FROM ProductTable WHERE productNo = ?";
    try (PreparedStatement stmt = theCon.prepareStatement(query)) {
      stmt.setString(1, pNum);
      try (ResultSet rs = stmt.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new StockException("SQL exists: " + e.getMessage());
    }
  }

  @Override
  public synchronized Product getDetails(String pNum) throws StockException {
    String query = """
            SELECT ProductTable.productNo, ProductTable.description, ProductTable.price, StockTable.stockLevel 
            FROM ProductTable
            JOIN StockTable ON ProductTable.productNo = StockTable.productNo
            WHERE ProductTable.productNo = ?
        """;
    try (PreparedStatement stmt = theCon.prepareStatement(query)) {
      stmt.setString(1, pNum);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return new Product(
                  rs.getString("productNo"),
                  rs.getString("description"),
                  rs.getDouble("price"),
                  rs.getInt("stockLevel")
          );
        }
        throw new StockException("Product not found: " + pNum);
      }
    } catch (SQLException e) {
      throw new StockException("SQL getDetails: " + e.getMessage());
    }
  }

  @Override
  public synchronized List<Product> getAllProducts() throws StockException {
    List<Product> products = new ArrayList<>();
    String query = """
            SELECT ProductTable.productNo, ProductTable.description, ProductTable.price, StockTable.stockLevel
            FROM ProductTable
            JOIN StockTable ON ProductTable.productNo = StockTable.productNo
        """;
    try (PreparedStatement stmt = theCon.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        products.add(new Product(
                rs.getString("productNo"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("stockLevel")
        ));
      }
    } catch (SQLException e) {
      throw new StockException("SQL getAllProducts: " + e.getMessage());
    }
    return products;
  }

  @Override
  public synchronized List<Product> searchProducts(String query) throws StockException {
    List<Product> products = new ArrayList<>();
    String sqlQuery = """
            SELECT ProductTable.productNo, ProductTable.description, ProductTable.price, StockTable.stockLevel
            FROM ProductTable
            JOIN StockTable ON ProductTable.productNo = StockTable.productNo
            WHERE ProductTable.description LIKE ?
        """;
    try (PreparedStatement stmt = theCon.prepareStatement(sqlQuery)) {
      stmt.setString(1, "%" + query + "%");
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          products.add(new Product(
                  rs.getString("productNo"),
                  rs.getString("description"),
                  rs.getDouble("price"),
                  rs.getInt("stockLevel")
          ));
        }
      }
    } catch (SQLException e) {
      throw new StockException("SQL searchProducts: " + e.getMessage());
    }
    return products;
  }

  @Override
  public synchronized ImageIcon getImage(String pNum) throws StockException {
    String query = "SELECT picture FROM ProductTable WHERE productNo = ?";
    String filename = "default.jpg";
    try (PreparedStatement stmt = theCon.prepareStatement(query)) {
      stmt.setString(1, pNum);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          filename = rs.getString("picture");
        }
      }
    } catch (SQLException e) {
      throw new StockException("SQL getImage: " + e.getMessage());
    }
    return new ImageIcon(filename);
  }
}

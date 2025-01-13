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
  private Connection theCon = null; // Connection to database
  private Statement theStmt = null; // Statement object

  /**
   * Connects to database
   * Uses a factory method to help setup the connection
   *
   * @throws StockException if problem
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

      theStmt = theCon.createStatement();
      theCon.setAutoCommit(true);
    } catch (SQLException e) {
      throw new StockException("SQL problem: " + e.getMessage());
    } catch (Exception e) {
      throw new StockException("Cannot load database driver.");
    }
  }

  /**
   * Returns a statement object that is used to process SQL statements
   *
   * @return A statement object used to access the database
   */
  protected Statement getStatementObject() {
    return theStmt;
  }

  /**
   * Returns a connection object that is used to process
   * requests to the DataBase
   *
   * @return a connection object
   */
  protected Connection getConnectionObject() {
    return theCon;
  }

  /**
   * Checks if the product exists in the stock list
   *
   * @param pNum The product number
   * @return true if exists otherwise false
   */
  public synchronized boolean exists(String pNum) throws StockException {
    try {
      ResultSet rs = getStatementObject().executeQuery(
              "SELECT price FROM ProductTable " +
                      "WHERE ProductTable.productNo = '" + pNum + "'"
      );
      boolean res = rs.next();
      DEBUG.trace("DB StockR: exists(%s) -> %s", pNum, (res ? "T" : "F"));
      return res;
    } catch (SQLException e) {
      throw new StockException("SQL exists: " + e.getMessage());
    }
  }

  /**
   * Returns details about the product in the stock list.
   * Assumed to exist in database.
   *
   * @param pNum The product number
   * @return Details in an instance of a Product
   */
  public synchronized Product getDetails(String pNum) throws StockException {
    try {
      Product dt = new Product("0", "", 0.00, 0);
      ResultSet rs = getStatementObject().executeQuery(
              "SELECT ProductTable.productNo, ProductTable.description, ProductTable.price, StockTable.stockLevel " +
                      "FROM ProductTable " +
                      "JOIN StockTable ON ProductTable.productNo = StockTable.productNo " +
                      "WHERE ProductTable.productNo = '" + pNum + "'"
      );
      if (rs.next()) {
        dt.setProductNum(pNum);
        dt.setDescription(rs.getString("description"));
        dt.setPrice(rs.getDouble("price"));
        dt.setQuantity(rs.getInt("stockLevel"));
      }
      rs.close();
      return dt;
    } catch (SQLException e) {
      throw new StockException("SQL getDetails: " + e.getMessage());
    }
  }


  /**
   * Returns all products in the stock list.
   *
   * @return A list of all available products.
   * @throws StockException if there is an SQL issue.
   */
  @Override
  public synchronized List<Product> getAllProducts() throws StockException {
    List<Product> products = new ArrayList<>();
    try {
      System.out.println("Executing query to fetch all products...");
      ResultSet rs = getStatementObject().executeQuery(
              "SELECT ProductTable.productNo, ProductTable.description, ProductTable.price, StockTable.stockLevel " +
                      "FROM ProductTable " +
                      "JOIN StockTable ON ProductTable.productNo = StockTable.productNo"
      );

      while (rs.next()) {
        products.add(new Product(
                rs.getString("productNo"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("stockLevel")
        ));
      }
      rs.close();
    } catch (SQLException e) {
      throw new StockException("SQL getAllProducts: " + e.getMessage());
    }
    return products;
  }




  /**
   * Searches for products in the stock list based on a query.
   *
   * @param query The search query.
   * @return A list of products that match the query.
   * @throws StockException if there is an SQL issue.
   */
  @Override
  public synchronized List<Product> searchProducts(String query) throws StockException {
    List<Product> products = new ArrayList<>();
    try {
      String sqlQuery = "SELECT ProductTable.productNo, ProductTable.description, ProductTable.price, StockTable.stockLevel " +
              "FROM ProductTable " +
              "JOIN StockTable ON ProductTable.productNo = StockTable.productNo " +
              "WHERE ProductTable.description LIKE ?";
      PreparedStatement stmt = theCon.prepareStatement(sqlQuery);
      stmt.setString(1, "%" + query + "%");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        products.add(new Product(
                rs.getString("productNo"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getInt("stockLevel")
        ));
      }
      rs.close();
    } catch (SQLException e) {
      throw new StockException("SQL searchProducts: " + e.getMessage());
    }
    return products;
  }


  /**
   * Returns 'image' of the product
   *
   * @param pNum The product number
   * Assumed to exist in database.
   * @return ImageIcon representing the image
   */
  public synchronized ImageIcon getImage(String pNum) throws StockException {
    String filename = "default.jpg";
    try {
      ResultSet rs = getStatementObject().executeQuery(
              "SELECT picture FROM ProductTable " +
                      "WHERE ProductTable.productNo = '" + pNum + "'"
      );

      boolean res = rs.next();
      if (res)
        filename = rs.getString("picture");
      rs.close();
    } catch (SQLException e) {
      DEBUG.error("getImage()\n%s\n", e.getMessage());
      throw new StockException("SQL getImage: " + e.getMessage());
    }

    // DEBUG.trace("DB StockR: getImage -> %s", filename);
    return new ImageIcon(filename);
  }
}

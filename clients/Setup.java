package clients;

import dbAccess.DBAccess;
import dbAccess.DBAccessFactory;

import java.sql.*;
import java.util.ArrayList;

class Setup {
  private static final String[] sqlStatements = {
          // Drop tables if they exist
          "DROP TABLE StockTable",
          "DROP TABLE ProductTable",

          // Create ProductTable
          "CREATE TABLE ProductTable (" +
                  "productNo CHAR(4) PRIMARY KEY," +
                  "description VARCHAR(40)," +
                  "picture VARCHAR(80)," +
                  "price FLOAT)",

          // Create StockTable with a foreign key to ProductTable
          "CREATE TABLE StockTable (" +
                  "productNo CHAR(4) PRIMARY KEY," +
                  "stockLevel INTEGER," +
                  "FOREIGN KEY (productNo) REFERENCES ProductTable(productNo))",

          // Insert sample products into ProductTable
          "INSERT INTO ProductTable VALUES ('0001', '40 inch LED HD TV', 'images/pic0001.jpg', 269.00)",
          "INSERT INTO ProductTable VALUES ('0002', 'DAB Radio', 'images/pic0002.jpg', 29.99)",
          "INSERT INTO ProductTable VALUES ('0003', 'Toaster', 'images/pic0003.jpg', 19.99)",
          "INSERT INTO ProductTable VALUES ('0004', 'Watch', 'images/pic0004.jpg', 29.99)",

          // Insert stock levels for products into StockTable
          "INSERT INTO StockTable VALUES ('0001', 90)",
          "INSERT INTO StockTable VALUES ('0002', 20)",
          "INSERT INTO StockTable VALUES ('0003', 33)",
          "INSERT INTO StockTable VALUES ('0004', 15)" // Added stock for the watch
  };


  public static void main(String[] args) {
    Connection theCon = null;
    DBAccess dbDriver = null;

    DBAccessFactory.setAction("Create");
    System.out.println("Setting up the CatShop database...");

    try {
      dbDriver = (new DBAccessFactory()).getNewDBAccess();
      dbDriver.loadDriver();
      theCon = DriverManager.getConnection(
              dbDriver.urlOfDatabase(),
              dbDriver.username(),
              dbDriver.password()
      );
      System.out.println("Connected to database successfully.");

      try (Statement stmt = theCon.createStatement()) {
        for (String sql : sqlStatements) {
          try {
            System.out.println("Executing: " + sql);
            stmt.execute(sql);
          } catch (SQLException e) {
            System.err.println("Error executing: " + sql);
            System.err.println("SQLException: " + e.getMessage());
          }
        }
      }
    } catch (SQLException e) {
      System.err.println("SQLException: " + e.getMessage());
      System.err.println("SQLState: " + e.getSQLState());
      System.err.println("VendorError: " + e.getErrorCode());
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    } finally {
      if (theCon != null) {
        try {
          theCon.close();
          DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
          if (e.getSQLState().equals("XJ015")) {
            System.out.println("Derby shut down successfully.");
          } else {
            System.err.println("Error shutting down Derby: " + e.getMessage());
          }
        }
      }
    }
  }
}

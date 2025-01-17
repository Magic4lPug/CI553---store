package clients;

import dbAccess.DBAccess;
import dbAccess.DBAccessFactory;

import java.sql.*;

class Setup {

  private static final String[] sqlStatements = {
          "DROP TABLE IF EXISTS StockTable;",
          "DROP TABLE IF EXISTS ProductTable;",
          "DROP TABLE IF EXISTS UserTable;",
          "DROP TABLE IF EXISTS BasketTable;",
          "DROP TABLE IF EXISTS TaskTable;",

          "CREATE TABLE TaskTable (" +
                  "taskID INTEGER PRIMARY KEY AUTOINCREMENT," +
                  "taskData BLOB NOT NULL," +
                  "status TEXT NOT NULL CHECK (status IN ('Unclaimed', 'Processing', 'Packed', 'Completed')));",


          "CREATE TABLE ProductTable (" +
                  "productNo TEXT PRIMARY KEY," +
                  "description TEXT," +
                  "picture TEXT," +
                  "price REAL);",

          "CREATE TABLE StockTable (" +
                  "productNo TEXT PRIMARY KEY," +
                  "stockLevel INTEGER," +
                  "FOREIGN KEY (productNo) REFERENCES ProductTable(productNo));",

          "CREATE TABLE UserTable (" +
                  "userID TEXT PRIMARY KEY," +
                  "username TEXT NOT NULL," +
                  "password TEXT NOT NULL," +
                  "salt TEXT NOT NULL," +
                  "email TEXT NOT NULL," +
                  "role TEXT);",

          "CREATE TABLE BasketTable (" +
                  "userID TEXT NOT NULL," +
                  "basketData BLOB," +
                  "PRIMARY KEY (userID)," +
                  "FOREIGN KEY (userID) REFERENCES UserTable(userID));",

          // Insert products
          "INSERT INTO ProductTable VALUES ('0001', '40 inch LED HD TV', 'images/pic0001.jpg', 269.00);",
          "INSERT INTO ProductTable VALUES ('0002', 'DAB Radio', 'images/pic0002.jpg', 29.99);",
          "INSERT INTO ProductTable VALUES ('0003', 'Toaster', 'images/pic0003.jpg', 19.99);",
          "INSERT INTO ProductTable VALUES ('0004', 'Watch', 'images/pic0004.jpg', 49.99);",
          "INSERT INTO ProductTable VALUES ('0005', 'Wireless Headphones', 'images/pic0005.jpg', 89.99);",
          "INSERT INTO ProductTable VALUES ('0006', 'Smartphone', 'images/pic0006.jpg', 499.00);",
          "INSERT INTO ProductTable VALUES ('0007', 'USB', 'images/pic0007.jpg', 5.00);",
          "INSERT INTO ProductTable VALUES ('0008', 'Laptop', 'images/pic0008.jpg', 899.00);",
          "INSERT INTO ProductTable VALUES ('0009', 'Nintendo Switch', 'images/pic0009.jpg', 399.00);",
          "INSERT INTO ProductTable VALUES ('0010', 'Table', 'images/pic0010.jpg', 120.00);",
          "INSERT INTO ProductTable VALUES ('0011', 'Chair', 'images/pic0011.jpg', 40.00);",
          "INSERT INTO ProductTable VALUES ('0012', 'Coat', 'images/pic0012.jpg', 130.00);",
          "INSERT INTO ProductTable VALUES ('0013', 'Trainers', 'images/pic0013.jpg', 60.00);",
          "INSERT INTO ProductTable VALUES ('0014', '5KG Weights', 'images/pic0014.jpg', 10.00);",
          "INSERT INTO ProductTable VALUES ('0015', 'Fireworks', 'images/pic0015.jpg', 80.00);",
          "INSERT INTO ProductTable VALUES ('0016', 'Teddy', 'images/pic0016.jpg', 20.00);",

          // Insert stock
          "INSERT INTO StockTable VALUES ('0001', 90);",
          "INSERT INTO StockTable VALUES ('0002', 50);",
          "INSERT INTO StockTable VALUES ('0003', 40);",
          "INSERT INTO StockTable VALUES ('0004', 30);",
          "INSERT INTO StockTable VALUES ('0005', 20);",
          "INSERT INTO StockTable VALUES ('0006', 15);",
          "INSERT INTO StockTable VALUES ('0007', 10);",
          "INSERT INTO StockTable VALUES ('0008', 5);",
          "INSERT INTO StockTable VALUES ('0009', 10);",
          "INSERT INTO StockTable VALUES ('0010', 20);",
          "INSERT INTO StockTable VALUES ('0011', 30);",
          "INSERT INTO StockTable VALUES ('0012', 40);",
          "INSERT INTO StockTable VALUES ('0013', 50);",
          "INSERT INTO StockTable VALUES ('0014', 60);",
          "INSERT INTO StockTable VALUES ('0015', 70);",
          "INSERT INTO StockTable VALUES ('0016', 80);",

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

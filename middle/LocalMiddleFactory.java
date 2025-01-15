/**
 * @author  Mike Smith University of Brighton
 * @version 2.2
 */

package middle;

import dbAccess.StockR;
import dbAccess.StockRW;
import orders.Order;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Provide access to middle tier components.
 * Now only one instance of each middle tier object is created.
 */

// Pattern: Abstract Factory

public class LocalMiddleFactory implements MiddleFactory {
  private static StockR aStockR = null;
  private static StockRW aStockRW = null;
  private static Order aOrder = null;
  private static Connection databaseConnection = null;
  private static final String DATABASE_URL = "jdbc:sqlite:catshop.db"; // Default database URL

  /**
   * Return an object to access the database for read-only access.
   * All users share this same object.
   */
  @Override
  public StockReader makeStockReader() throws StockException {
    if (aStockR == null) {
      aStockR = new StockR();
    }
    return aStockR;
  }

  /**
   * Return an object to access the database for read/write access.
   * All users share this same object.
   */
  @Override
  public StockReadWriter makeStockReadWriter() throws StockException {
    if (aStockRW == null) {
      aStockRW = new StockRW();
    }
    return aStockRW;
  }

  /**
   * Return an object to access the order processing system.
   * All users share this same object.
   */
  @Override
  public OrderProcessing makeOrderProcessing() throws OrderException {
    if (aOrder == null) {
      aOrder = new Order();
    }
    return aOrder;
  }

  /**
   * Return a Connection object to access the database.
   */
  public Connection makeDatabaseConnection() throws Exception {
    if (databaseConnection == null) {
      try {
        databaseConnection = DriverManager.getConnection(DATABASE_URL);
      } catch (Exception e) {
        throw new RuntimeException("Failed to connect to the database: " + e.getMessage());
      }
    }
    return databaseConnection;
  }
}

package dbAccess;

import catalogue.Product;
import debug.DEBUG;
import middle.StockException;
import middle.StockReadWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements read/write access to the stock database.
 */
public class StockRW extends StockR implements StockReadWriter {

  /*
   * Connects to database using StockR's constructor
   */
  public StockRW() throws StockException {
    super(); // Connection done in StockR's constructor
  }

  /**
   * Customer buys stock, quantity decreased if successful.
   * @param pNum Product number
   * @param amount Amount of stock bought
   * @return true if succeeds else false
   */
  public synchronized boolean buyStock(String pNum, int amount) throws StockException {
    DEBUG.trace("DB StockRW: buyStock(%s,%d)", pNum, amount);
    String updateStock = "UPDATE StockTable SET stockLevel = stockLevel - ? WHERE productNo = ? AND stockLevel >= ?";
    try (PreparedStatement stmt = theCon.prepareStatement(updateStock)) {
      stmt.setInt(1, amount);
      stmt.setString(2, pNum);
      stmt.setInt(3, amount);
      int updates = stmt.executeUpdate();
      return updates > 0; // Success if at least one row was updated
    } catch (SQLException e) {
      throw new StockException("SQL buyStock: " + e.getMessage());
    }
  }

  /**
   * Adds stock (Re-stocks) to the store.
   * Assumed to exist in database.
   * @param pNum Product number
   * @param amount Amount of stock to add
   */
  public synchronized void addStock(String pNum, int amount) throws StockException {
    String updateStock = "UPDATE StockTable SET stockLevel = stockLevel + ? WHERE productNo = ?";
    try (PreparedStatement stmt = theCon.prepareStatement(updateStock)) {
      stmt.setInt(1, amount);
      stmt.setString(2, pNum);
      stmt.executeUpdate();
      DEBUG.trace("DB StockRW: addStock(%s,%d)", pNum, amount);
    } catch (SQLException e) {
      throw new StockException("SQL addStock: " + e.getMessage());
    }
  }

  /**
   * Modifies Stock details for a given product number.
   * Information modified: Description, Price
   * @param detail Product details to change stocklist to
   */
  @Override
  public synchronized void modifyStock(Product detail) throws StockException {
    String insertProduct = "INSERT OR REPLACE INTO ProductTable (productNo, description, picture, price) VALUES (?, ?, ?, ?)";
    String insertStock = "INSERT OR REPLACE INTO StockTable (productNo, stockLevel) VALUES (?, ?)";

    try (PreparedStatement productStmt = theCon.prepareStatement(insertProduct);
         PreparedStatement stockStmt = theCon.prepareStatement(insertStock)) {

      productStmt.setString(1, detail.getProductNum());
      productStmt.setString(2, detail.getDescription());
      productStmt.setString(3, "images/Pic" + detail.getProductNum() + ".jpg");
      productStmt.setDouble(4, detail.getPrice());
      productStmt.executeUpdate();

      stockStmt.setString(1, detail.getProductNum());
      stockStmt.setInt(2, detail.getQuantity());
      stockStmt.executeUpdate();

    } catch (SQLException e) {
      throw new StockException("SQL modifyStock: " + e.getMessage());
    }
  }
}

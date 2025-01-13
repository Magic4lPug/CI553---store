package remote;

import catalogue.Product;
import dbAccess.StockRW;
import middle.StockException;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Implements Read/Write access to the stock list,
 * the stock list is held in a relational DataBase.
 * @author  Mike Smith University of Brighton
 * @version 2.1
 */
public class R_StockRW
        extends java.rmi.server.UnicastRemoteObject
        implements RemoteStockRW_I {

  private static final long serialVersionUID = 1;
  private StockRW aStockRW = null;

  /**
   * All transactions are done via StockRW to ensure
   * that a single connection to the database is used for all transactions
   * @param url of remote object
   * @throws java.rmi.RemoteException if issue
   * @throws middle.StockException if issue
   */
  public R_StockRW(String url) throws RemoteException, StockException {
    aStockRW = new StockRW();
  }

  @Override
  public synchronized boolean exists(String pNum) throws StockException {
    return aStockRW.exists(pNum);
  }

  @Override
  public synchronized Product getDetails(String pNum) throws StockException {
    return aStockRW.getDetails(pNum);
  }

  @Override
  public synchronized ImageIcon getImage(String pNum) throws StockException {
    return aStockRW.getImage(pNum);
  }

  @Override
  public synchronized boolean buyStock(String pNum, int amount) throws StockException {
    return aStockRW.buyStock(pNum, amount);
  }

  @Override
  public synchronized void addStock(String pNum, int amount) throws StockException {
    aStockRW.addStock(pNum, amount);
  }

  @Override
  public synchronized void modifyStock(Product product) throws StockException {
    aStockRW.modifyStock(product);
  }

  /**
   * Returns all products in the stock list.
   * @return List of all available products.
   * @throws middle.StockException if an underlying error occurs.
   */
  @Override
  public synchronized List<Product> getAllProducts() throws StockException {
    return aStockRW.getAllProducts();
  }

  /**
   * Searches for products in the stock list based on a query.
   * @param query The search query.
   * @return A list of products that match the query.
   * @throws middle.StockException if an underlying error occurs.
   */
  @Override
  public synchronized List<Product> searchProducts(String query) throws StockException {
    return aStockRW.searchProducts(query);
  }
}

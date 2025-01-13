package middle;

import catalogue.Product;
import debug.DEBUG;
import remote.RemoteStockR_I;

import javax.swing.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Facade for read access to the stock list.
 */
public class F_StockR implements StockReader {
  private RemoteStockR_I aR_StockR = null;
  private String theStockURL = null;

  public F_StockR(String url) {
    DEBUG.trace("F_StockR: %s", url);
    theStockURL = url;
  }

  private void connect() throws StockException {
    try {
      aR_StockR = (RemoteStockR_I) Naming.lookup(theStockURL);
    } catch (Exception e) {
      aR_StockR = null;
      throw new StockException("Com: " + e.getMessage());
    }
  }

  @Override
  public synchronized boolean exists(String number) throws StockException {
    try {
      if (aR_StockR == null) connect();
      return aR_StockR.exists(number);
    } catch (RemoteException e) {
      aR_StockR = null;
      throw new StockException("Net: " + e.getMessage());
    }
  }

  @Override
  public synchronized Product getDetails(String number) throws StockException {
    try {
      if (aR_StockR == null) connect();
      return aR_StockR.getDetails(number);
    } catch (RemoteException e) {
      aR_StockR = null;
      throw new StockException("Net: " + e.getMessage());
    }
  }

  @Override
  public synchronized List<Product> getAllProducts() throws StockException {
    try {
      if (aR_StockR == null) connect();
      return aR_StockR.getAllProducts();
    } catch (RemoteException e) {
      aR_StockR = null;
      throw new StockException("Net: " + e.getMessage());
    }
  }

  @Override
  public synchronized List<Product> searchProducts(String query) throws StockException {
    try {
      if (aR_StockR == null) connect();
      return aR_StockR.searchProducts(query);
    } catch (RemoteException e) {
      aR_StockR = null;
      throw new StockException("Net: " + e.getMessage());
    }
  }

  @Override
  public synchronized ImageIcon getImage(String number) throws StockException {
    try {
      if (aR_StockR == null) connect();
      return aR_StockR.getImage(number);
    } catch (RemoteException e) {
      aR_StockR = null;
      throw new StockException("Net: " + e.getMessage());
    }
  }
}

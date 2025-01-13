package remote;

import catalogue.Product;
import dbAccess.StockR;
import middle.StockException;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Implements Read access to the stock list, using RMI.
 */
public class R_StockR extends java.rmi.server.UnicastRemoteObject implements RemoteStockR_I {
  private static final long serialVersionUID = 1L;
  private final StockR aStockR;

  public R_StockR(String url) throws RemoteException, StockException {
    aStockR = new StockR();
  }

  @Override
  public synchronized boolean exists(String pNum) throws RemoteException, StockException {
    return aStockR.exists(pNum);
  }

  @Override
  public synchronized Product getDetails(String pNum) throws RemoteException, StockException {
    return aStockR.getDetails(pNum);
  }

  @Override
  public synchronized List<Product> getAllProducts() throws RemoteException, StockException {
    return aStockR.getAllProducts();
  }

  @Override
  public synchronized List<Product> searchProducts(String query) throws RemoteException, StockException {
    return aStockR.searchProducts(query);
  }

  @Override
  public synchronized ImageIcon getImage(String pNum) throws RemoteException, StockException {
    return aStockR.getImage(pNum);
  }
}

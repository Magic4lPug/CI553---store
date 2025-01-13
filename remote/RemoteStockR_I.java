package remote;

import catalogue.Product;
import middle.StockException;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface for stock reading.
 */
public interface RemoteStockR_I extends Remote {
  boolean exists(String pNum) throws RemoteException, StockException;

  Product getDetails(String pNum) throws RemoteException, StockException;

  List<Product> getAllProducts() throws RemoteException, StockException;

  List<Product> searchProducts(String query) throws RemoteException, StockException;

  ImageIcon getImage(String pNum) throws RemoteException, StockException;
}

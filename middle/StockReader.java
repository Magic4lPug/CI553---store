package middle;

import catalogue.Product;

import javax.swing.*;
import java.util.List;

public interface StockReader {
 boolean exists(String pNum) throws StockException;

 Product getDetails(String pNum) throws StockException;

 List<Product> getAllProducts() throws StockException;

 List<Product> searchProducts(String query) throws StockException;

 ImageIcon getImage(String pNum) throws StockException;
}

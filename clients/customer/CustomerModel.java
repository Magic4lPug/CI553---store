package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.SharedOrderQueue;
import middle.StockException;
import middle.StockReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Implements the Model of the customer client.
 */
public class CustomerModel extends Observable {
  private final Basket theBasket = new Basket(); // Customer's basket
  private final StockReader theStock;

  private List<Product> products = new ArrayList<>(); // List of products
  private String errorMessage = ""; // Error message

  public CustomerModel(MiddleFactory mf) {
    try {
      theStock = mf.makeStockReader(); // Connect to the stock database
      fetchAllProducts(); // Fetch all products at startup
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize CustomerModel: " + e.getMessage());
    }
  }

  public void fetchAllProducts() {
    try {
      products = theStock.getAllProducts(); // Fetch products from the stock
      setChanged(); // Mark the observable as changed
      notifyObservers(); // Notify observers to refresh the view
    } catch (StockException e) {
      notifyObserversWithError("Failed to fetch products: " + e.getMessage());
    }
  }

  public void searchProducts(String query) {
    try {
      products = theStock.searchProducts(query); // Fetch products based on query
      setChanged();
      notifyObservers(); // Notify observers to refresh the view
    } catch (StockException e) {
      notifyObserversWithError("Search failed: " + e.getMessage());
    }
  }

  public void addToBasket(Product product) {
    theBasket.add(product);
    setChanged();
    notifyObservers("Product added to basket.");
  }

  public Basket getBasket() {
    return theBasket;
  }

  public List<Product> getProducts() {
    return products;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public boolean hasError() {
    return !errorMessage.isEmpty();
  }

  private void notifyObserversWithError(String errorMessage) {
    this.errorMessage = errorMessage;
    setChanged();
    notifyObservers();
  }

  public boolean checkoutBasket() {
    try {
      SharedOrderQueue.addOrder(new Basket(theBasket)); // Add a copy of the basket to the queue
      theBasket.clear(); // Clear the customer's basket after checkout
      setChanged();
      notifyObservers("Basket checked out. Ready for new items.");
      return true;
    } catch (Exception e) {
      notifyObserversWithError("Checkout failed: " + e.getMessage());
      return false;
    }
  }
}

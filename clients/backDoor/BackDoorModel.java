package clients.backDoor;

import catalogue.Basket;
import catalogue.Product;
import clients.customer.CustomerModel;
import middle.StockReadWriter;

import java.util.Observable;

/**
 * Implements the Model of the BackDoor client.
 */
public class BackDoorModel extends Observable {
  private final StockReadWriter stock;
  private final Basket basket = new Basket();
  private CustomerModel customerModel;
  /**
   * Constructor for BackDoorModel.
   * @param stock The StockReadWriter instance.
   */
  public BackDoorModel(StockReadWriter stock) {
    this.stock = stock;
  }

  public void setCustomerModel(CustomerModel customerModel) {
    this.customerModel = customerModel;
  }


  /**
   * Get the current basket.
   * @return Basket instance.
   */
  public Basket getBasket() {
    return basket;
  }

  /**
   * Query the stock for a specific product.
   * @param productNum Product number to query.
   */
  public void doQuery(String productNum) {
    try {
      Product product = stock.getDetails(productNum);
      notifyAction(product.getDescription() + ": " + product.getQuantity() + " in stock.");
    } catch (Exception e) {
      notifyAction("Query failed: " + e.getMessage());
    }
  }

  /**
   * Restock a product with the given quantity.
   * @param productNum Product number to restock.
   * @param quantity Quantity to add.
   */
  public void doRestock(String productNum, String quantity) {
    try {
      int amount = Integer.parseInt(quantity.trim());
      if (amount <= 0) throw new NumberFormatException("Quantity must be positive.");
      if (stock.exists(productNum)) {
        stock.addStock(productNum, amount);
        notifyAction("Restocked " + amount + " items for product " + productNum);
        if (customerModel != null) {
          customerModel.fetchAllProducts(); // Refresh products
        }
      } else {
        notifyAction("Unknown product: " + productNum);
      }
    } catch (Exception e) {
      notifyAction("Restock failed: " + e.getMessage());
    }
  }

  /**
   * Remove stock of a product with the given quantity.
   * @param productNum Product number to remove stock from.
   * @param quantity Quantity to remove.
   */
  public void doRemoveStock(String productNum, String quantity) {
    try {
      int amount = Integer.parseInt(quantity.trim());
      if (amount <= 0) throw new NumberFormatException("Quantity must be positive.");
      if (stock.exists(productNum)) {
        stock.addStock(productNum, -amount);
        notifyAction("Removed " + amount + " items for product " + productNum);
        if (customerModel != null) {
          customerModel.fetchAllProducts(); // Refresh products
        }
      } else {
        notifyAction("Unknown product: " + productNum);
      }
    } catch (Exception e) {
      notifyAction("Remove stock failed: " + e.getMessage());
    }
  }

  /**
   * Notify observers with a specific action message.
   * @param action Action message to notify observers with.
   */
  private void notifyAction(String action) {
    setChanged();
    notifyObservers(action);
  }
}

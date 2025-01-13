package clients.customer;

import catalogue.Basket;
import catalogue.Product;

import javax.swing.*;
import java.util.List;

public class CustomerController {
  private final CustomerModel model;
  private final CustomerView view;

  public CustomerController(CustomerModel model, CustomerView view) {
    this.model = model;
    this.view = view;

    model.fetchAllProducts();
  }

  /**
   * Perform a search based on user input.
   * @param query Search query.
   */
  public void doSearch(String query) {
    model.searchProducts(query);
  }


  /**
   * View the customer's basket.
   */
  public void viewBasket() {
    Basket basket = model.getBasket();
    if (basket.isEmpty()) {
      JOptionPane.showMessageDialog(null, "Your basket is empty.", "Basket", JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    StringBuilder basketDetails = new StringBuilder("Your Basket:\n\n");
    for (Product product : basket) {
      basketDetails.append(String.format("Product: %s (%d units) - $%.2f\n",
              product.getDescription(),
              product.getQuantity(),
              product.getPrice() * product.getQuantity()));
    }

    JOptionPane.showMessageDialog(null, basketDetails.toString(), "Basket", JOptionPane.INFORMATION_MESSAGE);
  }


  public void addToBasket(Product product) {
    if (product != null) {
      model.addToBasket(product);
      JOptionPane.showMessageDialog(null, "Product added to basket!");
    }
  }

  public void checkoutBasket() {
    if (model.getBasket().isEmpty()) {
      JOptionPane.showMessageDialog(null, "Your basket is empty. Add items to the basket before checking out.");
      return;
    }

    boolean success = model.checkoutBasket();
    if (success) {
      JOptionPane.showMessageDialog(null, "Checkout successful! Your order has been sent to the cashier.");
    } else {
      JOptionPane.showMessageDialog(null, "Checkout failed. Please try again later.");
    }
  }

}

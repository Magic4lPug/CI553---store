package clients.customer;

import catalogue.Basket;
import catalogue.BasketController;
import catalogue.Product;

import javax.swing.*;
import java.sql.Connection;

public class CustomerController {
  private final CustomerModel model;
  private final CustomerView view;
  private final CustomerProfileHandler profileHandler;
  private final BasketController basketController;
  private final String userID; // Unique user identifier

  public CustomerController(CustomerModel model, CustomerView view, Connection databaseConnection, String userID, JFrame mainWindow) {
    this.model = model;
    this.view = view;
    this.userID = userID;

    // Initialize the profile handler dynamically with the userID and the main window reference
    this.profileHandler = new CustomerProfileHandler(userID, mainWindow);

    // Initialize the basket controller with the model's basket, database connection, and userID
    this.basketController = new BasketController(model.getBasket(), databaseConnection, userID);
    this.basketController.setCustomerModel(model); // Pass the model to the basket controller

    // Add the view as an observer
    model.addObserver(view);

    // Fetch all products and load the user's basket
    model.fetchAllProducts();
    loadBasket();
  }

  public void doSearch(String query) {
    model.searchProducts(query);
  }

  public void viewBasket() {
    if (basketController != null) {
      basketController.viewBasket();
    } else {
      JOptionPane.showMessageDialog(null, "Basket is not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void addToBasket(Product product, int quantity) {
    if (product != null && quantity > 0) {
      Product productToAdd = new Product(
              product.getProductNum(),
              product.getDescription(),
              product.getPrice(),
              quantity
      );
      model.addToBasket(productToAdd);
      JOptionPane.showMessageDialog(null, "Added " + quantity + " item(s) to basket!");
      saveBasket();
    } else {
      JOptionPane.showMessageDialog(null,
              "Invalid quantity. Please ensure it's greater than 0.",
              "Error",
              JOptionPane.ERROR_MESSAGE);
    }
  }

  public void checkoutBasket() {
    if (basketController != null) {
      basketController.checkoutBasket(); // Now updates stock levels and triggers view refresh
    } else {
      JOptionPane.showMessageDialog(null, "Basket is not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void removeItemFromBasket(Product product) {
    if (basketController != null) {
      basketController.removeFromBasket(product);
    } else {
      JOptionPane.showMessageDialog(null, "Basket is not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void viewProfile() {
    if (profileHandler != null) {
      profileHandler.showProfile();
    } else {
      JOptionPane.showMessageDialog(null, "Profile handler is not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void saveBasket() {
    if (basketController != null) {
      basketController.saveBasket();
    } else {
      JOptionPane.showMessageDialog(null, "Basket is not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  public void loadBasket() {
    if (basketController != null) {
      basketController.loadBasket();
    } else {
      JOptionPane.showMessageDialog(null, "Basket is not initialized.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}

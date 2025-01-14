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

  public CustomerController(CustomerModel model, CustomerView view, Connection databaseConnection, String userID) {
    this.model = model;
    this.view = view;
    this.userID = userID;

    // Initialize the profile handler with user details
    this.profileHandler = new CustomerProfileHandler(userID, "john.doe@example.com");

    // Initialize the basket controller with the model's basket, database connection, and userID
    this.basketController = new BasketController(model.getBasket(), databaseConnection, userID);

    model.addObserver(view); // Add the view as an observer
    model.fetchAllProducts(); // Fetch all products on initialization

    // Load the user's basket
    loadBasket();
  }

  public void doSearch(String query) {
    model.searchProducts(query);
  }

  public void viewBasket() {
    basketController.viewBasket();
  }

  public void addToBasket(Product product) {
    if (product != null) {
      model.addToBasket(product);
      JOptionPane.showMessageDialog(null, "Product added to basket!");
      saveBasket(); // Save basket after adding a product
    }
  }

  public void checkoutBasket() {
    basketController.checkoutBasket(); // `userID` now passed during initialization
  }

  public void removeItemFromBasket(Product product) {
    basketController.removeFromBasket(product); // `userID` now managed internally in BasketController
  }

  public void viewProfile() {
    profileHandler.showProfile();
  }

  public void saveBasket() {
    basketController.saveBasket(); // `userID` now managed internally in BasketController
  }

  public void loadBasket() {
    basketController.loadBasket(); // `userID` now managed internally in BasketController
  }
}

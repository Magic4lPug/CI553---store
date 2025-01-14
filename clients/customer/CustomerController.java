package clients.customer;

import catalogue.Basket;
import catalogue.BasketController;
import catalogue.Product;

import javax.swing.*;

public class CustomerController {
  private final CustomerModel model;
  private final CustomerView view;
  private final CustomerProfileHandler profileHandler; // Profile handler
  private final BasketController basketController; // Basket controller for managing the basket

  public CustomerController(CustomerModel model, CustomerView view) {
    this.model = model;
    this.view = view;

    // Initialize the profile handler with user details
    this.profileHandler = new CustomerProfileHandler("john_doe", "john.doe@example.com");

    // Initialize the basket controller
    this.basketController = new BasketController(model.getBasket());

    model.addObserver(view); // Add the view as an observer
    model.fetchAllProducts(); // Fetch all products on initialization
  }

  public void doSearch(String query) {
    model.searchProducts(query);
  }

  public void viewBasket() {
    // Delegate to BasketController to show the basket view
    basketController.viewBasket();
  }

  public void addToBasket(Product product) {
    if (product != null) {
      model.addToBasket(product);
      JOptionPane.showMessageDialog(null, "Product added to basket!");
    }
  }

  public void checkoutBasket() {
    // Delegate checkout to BasketController
    basketController.checkoutBasket();
  }

  public void viewProfile() {
    profileHandler.showProfile(); // Delegate to CustomerProfileHandler
  }
}

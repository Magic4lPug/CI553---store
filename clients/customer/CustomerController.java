package clients.customer;

import catalogue.BasketController;
import catalogue.Product;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;

public class CustomerController {
  private final CustomerModel model;
  private final CustomerView view;
  private final CustomerProfileHandler profileHandler;
  private final BasketController basketController;
  private final String userID; // Unique user identifier

  public CustomerController(CustomerModel model, CustomerView view, Connection databaseConnection, String userID, Stage mainWindow) {
    this.model = model;
    this.view = view;
    this.userID = userID;

    // Initialise the profile handler dynamically with the userID and the main window reference
    this.profileHandler = new CustomerProfileHandler(userID, mainWindow);

    // Initialise the basket controller with the model's basket, database connection, and userID
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
      showAlert("Error", "Basket is not initialized.");
    }
  }

  public void addToBasket(Product product, int quantity) {
    if (product != null && quantity > 0) {
      Product productToAdd = new Product(
              product.getProductNum(),
              product.getDescription(),
              product.getPrice(),
              quantity,
              product.getPicture() // Include the picture field
      );
      model.addToBasket(productToAdd);
      showAlert("Success", "Added " + quantity + " item(s) to basket!");
      saveBasket();
    } else {
      showAlert("Error", "Invalid quantity. Please ensure it's greater than 0.");
    }
  }


  public void checkoutBasket() {
    if (basketController != null) {
      basketController.checkoutBasket(); // Updates stock levels and triggers view refresh
    } else {
      showAlert("Error", "Basket is not initialized.");
    }
  }

  public void removeItemFromBasket(Product product) {
    if (basketController != null) {
      basketController.removeFromBasket(product);
    } else {
      showAlert("Error", "Basket is not initialized.");
    }
  }

  public void viewProfile() {
    if (profileHandler != null) {
      profileHandler.showProfile();
    } else {
      showAlert("Error", "Profile handler is not initialized.");
    }
  }

  public void saveBasket() {
    if (basketController != null) {
      basketController.saveBasket();
    } else {
      showAlert("Error", "Basket is not initialized.");
    }
  }

  public void loadBasket() {
    if (basketController != null) {
      basketController.loadBasket();
    } else {
      showAlert("Error", "Basket is not initialized.");
    }
  }

  // Utility method to show alerts
  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}

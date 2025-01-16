package clients.customer;

import catalogue.Product;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CustomerView extends Stage implements Observer {
    private static final int H = 550; // Height of window pixels
    private static final int W = 700; // Width of window pixels

    private final Label pageTitle = new Label("The Treasure Trove");
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Search");
    private final TableView<Product> productTable = new TableView<>(); // Product list
    private final Button addToBasketButton = new Button("Add to Basket");
    private final Button basketButton = new Button("🛒 Basket");
    private final Label theAction = new Label();
    private final Button profileImg = new Button();

    private CustomerController cont;

    public CustomerView() {
        setTitle("Customer View - The Treasure Trove");
        setWidth(W);
        setHeight(H);

        // Load and configure the profile image button
        javafx.scene.image.Image profileImage = new javafx.scene.image.Image("images/profile_img.png"); // Replace with your profile image path
        javafx.scene.image.ImageView profileImageView = new javafx.scene.image.ImageView(profileImage);
        profileImageView.setFitWidth(130); // Set desired width (larger size)
        profileImageView.setFitHeight(130); // Set desired height (larger size)
        profileImg.setGraphic(profileImageView);
        profileImg.setStyle("-fx-background-color: transparent;"); // Transparent background
        profileImg.setPrefSize(100, 100); // Optional: Adjust button size


        // Load and configure the treasure trove logo
        javafx.scene.image.Image logoImage = new javafx.scene.image.Image("images/treasure_trove_logo.png"); // Replace with your logo image path
        javafx.scene.image.ImageView logoImageView = new javafx.scene.image.ImageView(logoImage);

        // Explicitly set the logo size
        logoImageView.setFitWidth(100); // Set desired width
        logoImageView.setFitHeight(100); // Set desired height

        // Page Title Styling
        pageTitle.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #FFD700;"); // Yellow color
        pageTitle.setAlignment(Pos.CENTER_LEFT); // Align title to the left
        // Search Field
        searchField.setPromptText("Search for products...");
        searchField.setPrefWidth(400);

        // Button Styling
        searchButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");
        basketButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");
        addToBasketButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");
        addToBasketButton.setPrefWidth(150);

        // Product Table Configuration
        TableColumn<Product, String> productNumCol = new TableColumn<>("Product No");
        productNumCol.setCellValueFactory(new PropertyValueFactory<>("productNum"));

        TableColumn<Product, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> stockLevelCol = new TableColumn<>("Stock Level");
        stockLevelCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        productTable.getColumns().addAll(productNumCol, descriptionCol, priceCol, stockLevelCol);
        productTable.setPrefHeight(300);

        // Action Label Styling
        theAction.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        theAction.setAlignment(Pos.CENTER);

        // Layout Configuration
        HBox topBar = new HBox(15, profileImg, pageTitle, logoImageView); // Use profileImg instead of profileButton
        topBar.setAlignment(Pos.CENTER); // Align the entire top bar to the left
        topBar.setSpacing(20); // Add spacing between elements
        HBox.setHgrow(pageTitle, Priority.ALWAYS); // Allow title to grow and take available space
        pageTitle.setAlignment(Pos.CENTER); // Center-align the title

        HBox searchBar = new HBox(10, searchField, searchButton, basketButton);
        searchBar.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, topBar, searchBar, productTable, addToBasketButton, theAction);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 15px; -fx-background-color: #2E2E2E;");

        Scene scene = new Scene(root, W, H);
        setScene(scene);
    }

    public void setController(CustomerController c) {
        this.cont = c;

        // Event Handlers
        Platform.runLater(() -> {
            searchButton.setOnAction(e -> cont.doSearch(searchField.getText()));
            basketButton.setOnAction(e -> cont.viewBasket());
            addToBasketButton.setOnAction(e -> {
                Product selectedProduct = getSelectedProduct();
                if (selectedProduct != null) {
                    showQuantityDialog(selectedProduct);
                } else {
                    showError("Please select a product first.");
                }
            });
            profileImg.setOnAction(e -> cont.viewProfile()); // Attach action to the profileImg button
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CustomerModel) {
            CustomerModel model = (CustomerModel) o;
            Platform.runLater(() -> {
                if (model.hasError()) {
                    showError(model.getErrorMessage());
                } else {
                    populateProductTable(model.getProducts());
                }
            });
        }
    }

    private void showQuantityDialog(Product product) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Select Quantity");
        dialog.setHeaderText("Enter quantity to add to basket:");
        dialog.setContentText("Available: " + product.getQuantity());

        dialog.showAndWait().ifPresent(quantityStr -> {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity > 0 && quantity <= product.getQuantity()) {
                    cont.addToBasket(product, quantity);
                } else {
                    showError("Invalid quantity. Please enter a number between 1 and " + product.getQuantity());
                }
            } catch (NumberFormatException ex) {
                showError("Invalid input. Please enter a valid number.");
            }
        });
    }

    private void populateProductTable(List<Product> products) {
        Platform.runLater(() -> {
            productTable.getItems().clear();
            productTable.getItems().addAll(products);
        });
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private Product getSelectedProduct() {
        return productTable.getSelectionModel().getSelectedItem();
    }


}

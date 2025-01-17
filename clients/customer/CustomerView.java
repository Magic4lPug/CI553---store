package clients.customer;

import catalogue.Product;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CustomerView extends Stage implements Observer {
    private static final int H = 650; // Height of window pixels
    private static final int W = 900; // Width of window pixels

    private final Label pageTitle = new Label("The Treasure Trove");
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button("Search");
    private final Button basketButton = new Button("\uD83D\uDED2 Basket");
    private final Label theAction = new Label();
    private final Button profileImg = new Button();
    private final ImageView logoImageView = new ImageView(new Image("images/treasure_trove_logo.png"));
    private final Button refreshButton = new Button("Refresh");

    private final TilePane productDisplay = new TilePane();
    private CustomerController cont;

    public CustomerView() {
        setTitle("Customer View - The Treasure Trove");
        setWidth(W);
        setHeight(H);

        // Load and configure the profile image button
        Image profileImage = new Image("images/profile_img.png");
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(120); // Adjust width for better size
        profileImageView.setFitHeight(120); // Adjust height for better size
        profileImg.setGraphic(profileImageView);
        profileImg.setStyle("-fx-background-color: transparent;"); // Transparent background

        // Page Title Styling
        pageTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #FFD700;"); // Yellow color

        // Configure logo image
        logoImageView.setFitWidth(120);
        logoImageView.setFitHeight(120);

        // Search Field
        searchField.setPromptText("Search for products...");
        searchField.setPrefWidth(400);

        // Button Styling
        searchButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");
        basketButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");

        // Configure ScrollPane for Product Display
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(productDisplay);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #2E2E2E; -fx-padding: 10px;");

        // Product Display Configuration
        productDisplay.setPrefTileWidth(150); // Smaller item width
        productDisplay.setPrefTileHeight(200); // Smaller item height
        productDisplay.setHgap(20); // Horizontal spacing
        productDisplay.setVgap(20); // Vertical spacing
        productDisplay.setPadding(new Insets(20));
        productDisplay.setAlignment(Pos.CENTER);
        productDisplay.setStyle("-fx-background-color: #2E2E2E;");

        // Action Label Styling
        theAction.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        theAction.setAlignment(Pos.CENTER);

        // Layout Configuration
        HBox topBar = new HBox(20, profileImg, pageTitle, logoImageView); // Add profileImg, title, and logo
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(20)); // Add padding for a cleaner layout
        topBar.setSpacing(30); // Increase spacing between elements

        HBox searchBar = new HBox(10, searchField, searchButton, basketButton,refreshButton);
        searchBar.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, topBar, searchBar, scrollPane, theAction);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 15px; -fx-background-color: #2E2E2E;");

        Scene scene = new Scene(root, W, H);
        setScene(scene);

        refreshButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");

    }

    public void setController(CustomerController c) {
        this.cont = c;

        // Event Handlers
        Platform.runLater(() -> {
            searchButton.setOnAction(e -> cont.doSearch(searchField.getText()));
            basketButton.setOnAction(e -> cont.viewBasket());
            profileImg.setOnAction(e -> cont.viewProfile()); // Attach action to the profileImg button
            refreshButton.setOnAction(e -> cont.refreshProducts());

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
                    populateProductDisplay(model.getProducts());
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

    private void showInspectWindow(Product product) {
        Stage inspectStage = new Stage();
        inspectStage.setTitle("Product Details");

        // Construct the image path dynamically based on the product ID
        String imagePath = "/images/pic" + product.getProductNum() + ".jpg";
        ImageView imageView;
        try {
            // Ensure the resource path starts with a forward slash
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                System.err.println("Image not found: " + imagePath);
                // Fallback to a default image if the specific image is not found
                imageStream = getClass().getResourceAsStream("/images/default.jpg");
            }
            Image image = new Image(imageStream);
            imageView = new ImageView(image);
        } catch (Exception e) {
            // Handle exceptions and load a default image
            imageView = new ImageView();
            imageView.setImage(new Image(getClass().getResourceAsStream("/images/default.jpg")));
        }
        imageView.setFitWidth(300);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(product.getDescription());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label priceLabel = new Label("Price: £" + product.getPrice());
        priceLabel.setStyle("-fx-font-size: 14px;");

        VBox layout = new VBox(20, imageView, nameLabel, priceLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(layout, 400, 500);
        inspectStage.setScene(scene);
        inspectStage.show();
    }


    private void populateProductDisplay(List<Product> products) {
        productDisplay.getChildren().clear();
        for (Product product : products) {
            VBox productBlock = createProductBlock(product);
            productDisplay.getChildren().add(productBlock);
        }
    }

    private VBox createProductBlock(Product product) {
        VBox block = new VBox();
        block.setPadding(new Insets(10));
        block.setSpacing(10);
        block.setAlignment(Pos.CENTER);
        block.setStyle("-fx-background-color: #FFD700; -fx-border-color: #000; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        Label nameLabel = new Label(product.getDescription());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #000;");

        Label priceLabel = new Label("Price: £" + product.getPrice());
        priceLabel.setStyle("-fx-text-fill: #000;");

        Label stockLabel = new Label("Stock: " + product.getQuantity());
        stockLabel.setStyle("-fx-text-fill: #000;");

        Button selectButton = new Button("Add to Basket");
        selectButton.setStyle("-fx-background-color: #000; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
        selectButton.setOnAction(e -> showQuantityDialog(product));

        Button inspectButton = new Button("Inspect");
        inspectButton.setStyle("-fx-background-color: #000; -fx-text-fill: #FFD700; -fx-font-weight: bold;");
        inspectButton.setOnAction(e -> showInspectWindow(product));

        block.getChildren().addAll(nameLabel, priceLabel, stockLabel, selectButton, inspectButton);
        return block;
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
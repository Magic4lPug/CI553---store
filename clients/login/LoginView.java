package clients.login;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView {
    private final TextField loginField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Register");

    public Scene createScene(Stage primaryStage) {
        primaryStage.setTitle("The Treasure Trove - Login");

        // Branding Image
        Image logoImage = new Image("images/treasure_trove_logo.png"); // Replace with your image path
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150);
        logoView.setPreserveRatio(true);

        // Title Label
        Label titleLabel = new Label("Welcome to The Treasure Trove");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FFD700;"); // Gold color

        // Layout for Login Fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username/Email:");
        usernameLabel.setStyle("-fx-text-fill: #FFFFFF;"); // White text

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: #FFFFFF;");

        // Adjust text field size
        loginField.setPrefWidth(250);
        passwordField.setPrefWidth(250);

        grid.add(usernameLabel, 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        // Button container
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginButton, registerButton);

        // Button Styles
        loginButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");
        registerButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");

        loginButton.setPrefWidth(130); // Set preferred width
        loginButton.setPrefHeight(40); // Set preferred height

        registerButton.setPrefWidth(130); // Set preferred width
        registerButton.setPrefHeight(40); // Set preferred height

        // Root Layout
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #2E2E2E; -fx-padding: 20px;"); // Dark gray background
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, logoView, grid, buttonBox);

        return new Scene(root, 400, 450);
    }

    // Getters for UI elements
    public TextField getLoginField() {
        return loginField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}

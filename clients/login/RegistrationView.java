package clients.login;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistrationView {
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();
    private final TextField emailField = new TextField();
    private final Button registerButton = new Button("Register");
    private final Button cancelButton = new Button("Cancel");

    public Scene createScene(Stage registerStage) {
        registerStage.setTitle("The Treasure Trove - Register");

        // Title Label
        Label titleLabel = new Label("Register a New Account");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FFD700;"); // Gold text


        // Layout for Registration Fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label confirmPasswordLabel = new Label("Confirm Password:");
        Label emailLabel = new Label("Email:");

        usernameLabel.setStyle("-fx-text-fill: #FFFFFF;");
        passwordLabel.setStyle("-fx-text-fill: #FFFFFF;");
        confirmPasswordLabel.setStyle("-fx-text-fill: #FFFFFF;");
        emailLabel.setStyle("-fx-text-fill: #FFFFFF;");

        usernameField.setPrefWidth(250);
        passwordField.setPrefWidth(250);
        confirmPasswordField.setPrefWidth(250);
        emailField.setPrefWidth(250);

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);

        // Button container
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(registerButton, cancelButton);

        registerButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");
        cancelButton.setStyle("-fx-background-color: #FFD700; -fx-text-fill: #000000; -fx-font-weight: bold;");

        registerButton.setPrefWidth(150);
        registerButton.setPrefHeight(40);
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(40);

        // Root Layout
        VBox root = new VBox(20);
        root.setStyle("-fx-background-color: #2E2E2E; -fx-padding: 20px;");
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, grid, buttonBox);

        return new Scene(root, 400, 450);
    }

    // Getters for UI elements
    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}

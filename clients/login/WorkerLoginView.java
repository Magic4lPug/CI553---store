package clients.login;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class WorkerLoginView {

    private final WorkerLogin controller;

    public WorkerLoginView(WorkerLogin controller) {
        this.controller = controller;
    }

    public void show(Stage primaryStage) {
        // Create UI Elements
        Label passwordLabel = new Label("Worker Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        // Add Action for Login Button
        loginButton.setOnAction(e -> {
            String password = passwordField.getText();
            if (controller.authenticateWorker(password)) {
                controller.onLoginSuccess(primaryStage);
            } else {
                controller.showAlert("Authentication Failed", "Invalid worker password.");
            }
        });

        // Create Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(passwordLabel, 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(loginButton, 0, 1, 2, 1);

        // Apply Styling
        grid.setStyle("-fx-background-color: #2E2E2E; -fx-padding: 15;");
        passwordLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 14px;");
        passwordField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #FFD700;");
        loginButton.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFD700; -fx-font-weight: bold;");

        // Set Scene
        Scene scene = new Scene(grid, 330, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

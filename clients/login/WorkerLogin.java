package clients.login;

import clients.cashier.CashierClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class WorkerLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Worker Login");

        // Create UI Elements
        Label passwordLabel = new Label("Worker Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");

        // Add Action for Login Button
        loginButton.setOnAction(e -> {
            String password = passwordField.getText();
            if (authenticateWorker(password)) {
                primaryStage.close();
                CashierClient.main(new String[]{}); // Open Worker Client
            } else {
                showAlert("Authentication Failed", "Invalid worker password.");
            }
        });

        // Create Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(passwordLabel, 0, 0);
        grid.add(passwordField, 1, 0);
        grid.add(loginButton, 0, 1, 2, 1);

        // Set Scene
        Scene scene = new Scene(grid, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateWorker(String password) {
        // Replace with your worker authentication logic
        return "worker123".equals(password);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

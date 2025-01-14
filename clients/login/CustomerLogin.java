package clients.login;

import clients.customer.CustomerClient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CustomerLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Login");

        // Create UI Elements
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");

        // Add Action for Login Button
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (authenticateCustomer(username, password)) {
                System.out.println("Authentication successful. Launching CustomerClient.");
                primaryStage.close();
                CustomerClient.main(new String[]{}); // Open Customer Client
            } else {
                showAlert("Authentication Failed", "Invalid username or password.");
            }
        });

        // Create Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2, 2, 1);

        // Set Scene
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateCustomer(String username, String password) {
        // Replace with your customer authentication logic
        return "customer1".equals(username) && "password1".equals(password);
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

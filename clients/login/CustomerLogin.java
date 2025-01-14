package clients.login;

import clients.customer.CustomerClient;
import dbAccess.DBAccessFactory;
import dbAccess.UserAccess;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

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
        Button registerButton = new Button("Register");

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

        // Add Action for Register Button
        registerButton.setOnAction(e -> {
            RegistrationWindow registrationWindow = new RegistrationWindow();
            registrationWindow.start(new Stage()); // Open Registration Window
        });

        // Create Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);

        // Set Scene
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateCustomer(String username, String password) {
        try {
            DBAccessFactory.setAction("");
            Connection connection = (new DBAccessFactory()).getNewDBAccess().getConnection();
            UserAccess userAccess = new UserAccess(connection);
            return userAccess.authenticate(username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

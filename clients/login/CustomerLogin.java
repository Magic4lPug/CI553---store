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

public class CustomerLogin extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Login");

        // Create UI Elements
        Label loginLabel = new Label("Username/Email:");
        TextField loginField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Add Action for Login Button
        loginButton.setOnAction(e -> {
            String login = loginField.getText();
            String password = passwordField.getText();
            if (authenticateCustomer(login, password)) {
                System.out.println("Authentication successful. Launching CustomerClient.");
                primaryStage.close();
                launchCustomerClient(login); // Pass userID (login)
            } else {
                showAlert("Authentication Failed", "Invalid username/email or password.");
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
        grid.add(loginLabel, 0, 0);
        grid.add(loginField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);

        // Set Scene
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateCustomer(String login, String password) {
        try {
            DBAccessFactory.setAction("");
            Connection connection = (new DBAccessFactory()).getNewDBAccess().getConnection();
            UserAccess userAccess = new UserAccess(connection);

            // Check if login is email or username and authenticate accordingly
            if (login.contains("@")) {
                // Login as email
                return userAccess.authenticateWithEmail(login, password);
            } else {
                // Login as username
                return userAccess.authenticate(login, password);
            }
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

    private void launchCustomerClient(String userID) {
        // Launch CustomerClient with the authenticated userID
        CustomerClient.launchWithUser(userID);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

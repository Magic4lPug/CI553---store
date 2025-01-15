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

        // Debug: Log window visibility
        System.out.println("CustomerLogin window about to be displayed.");
        primaryStage.show();
        System.out.println("CustomerLogin window displayed.");
    }

    private boolean authenticateCustomer(String login, String password) {
        try {
            DBAccessFactory.setAction("");
            Connection connection = (new DBAccessFactory()).getNewDBAccess().getConnection();
            UserAccess userAccess = new UserAccess(connection);

            // Fetch the userID regardless of login type
            String userID = userAccess.getUserIDByLogin(login, password);

            if (userID != null) {
                launchCustomerClient(userID); // Use userID for operations
                return true;
            } else {
                showAlert("Authentication Failed", "Invalid username/email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred during authentication.");
        }
        return false;
    }

    private void launchCustomerClient(String userID) {
        CustomerClient.launchWithUser(userID); // Ensures only one instance is launched
    }

    public static void relaunch() {
        javafx.application.Platform.runLater(() -> {
            try {
                System.out.println("Attempting to relaunch CustomerLogin...");
                Stage loginStage = new Stage();
                CustomerLogin loginApp = new CustomerLogin();
                loginApp.start(loginStage);
                System.out.println("CustomerLogin relaunched.");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to relaunch CustomerLogin.");
            }
        });
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

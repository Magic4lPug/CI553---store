// Updated RegistrationWindow to include improved email validation and updated error message
package clients.login;

import dbAccess.DBAccessFactory;
import dbAccess.UserAccess;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import middle.StockException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RegistrationWindow extends Application {

    @Override
    public void start(Stage registerStage) {
        registerStage.setTitle("Register Account");

        // Create UI Elements
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Button registerButton = new Button("Register");
        Button cancelButton = new Button("Cancel");

        // Add Action for Register Button
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String email = emailField.getText();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                showAlert("Error", "All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("Error", "Passwords do not match.");
                return;
            }

            if (!isValidEmail(email)) {
                showAlert("Error", "Please include an email in the following format: example@domain.com");
                return;
            }

            try {
                DBAccessFactory.setAction("");
                Connection connection = (new DBAccessFactory()).getNewDBAccess().getConnection();
                UserAccess userAccess = new UserAccess(connection);

                if (userAccess.doesUsernameExist(username)) {
                    showAlert("Error", "Username already exists.");
                    return;
                }

                if (userAccess.doesEmailExist(email)) {
                    showAlert("Error", "Email already exists.");
                    return;
                }

                String userID = UUID.randomUUID().toString();
                userAccess.createUser(userID, username, password, email, "customer");
                showAlert("Success", "Registration successful!");
                registerStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error", "Failed to register. Please try again.");
            }
        });

        // Add Action for Cancel Button
        cancelButton.setOnAction(e -> registerStage.close());

        // Create Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(confirmPasswordLabel, 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(registerButton, 0, 4);
        grid.add(cancelButton, 1, 4);

        // Set Scene
        Scene scene = new Scene(grid, 350, 250);
        registerStage.setScene(scene);
        registerStage.show();
    }

    private boolean isValidEmail(String email) {
        // Improved regex for email validation
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package clients.login;

import dbAccess.DBAccessFactory;
import dbAccess.UserAccess;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.UUID;

public class RegistrationWindow extends Application {

    @Override
    public void start(Stage registerStage) {
        RegistrationView registrationView = new RegistrationView();
        registerStage.setScene(registrationView.createScene(registerStage));

        // Set up event handlers
        registrationView.getRegisterButton().setOnAction(e -> {
            String username = registrationView.getUsernameField().getText();
            String password = registrationView.getPasswordField().getText();
            String confirmPassword = registrationView.getConfirmPasswordField().getText();
            String email = registrationView.getEmailField().getText();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                showAlert("Error", "All fields are required.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("Error", "Passwords do not match.");
                return;
            }

            if (!isValidPassword(password)) {
                showAlert("Error", "Password must be at least 8 characters long and include a mix of uppercase letters, lowercase letters, numbers, and special characters.");
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

        registrationView.getCancelButton().setOnAction(e -> registerStage.close());

        registerStage.show();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{}|;:',.<>?/]).{8,}$";
        return password.matches(passwordPattern);
    }

    private boolean isValidEmail(String email) {
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

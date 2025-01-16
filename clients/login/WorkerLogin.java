package clients.login;

import clients.cashier.CashierClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class WorkerLogin extends Application {

    private WorkerLoginView view;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Worker Login");
        view = new WorkerLoginView(this);
        view.show(primaryStage);
    }

    public boolean authenticateWorker(String password) {
        // Replace with your worker authentication logic
        return "worker123".equals(password);
    }

    public void onLoginSuccess(Stage stage) {
        stage.close();
        CashierClient.main(new String[]{}); // Open Worker Client
    }

    public void showAlert(String title, String message) {
        view.showAlert(title, message);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

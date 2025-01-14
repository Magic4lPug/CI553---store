package clients;

import clients.login.CustomerLogin;
import clients.login.WorkerLogin;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    // Create and show the Customer Login window
    Stage customerLoginStage = new Stage();
    new CustomerLogin().start(customerLoginStage);

    // Create and show the Worker Login window
    Stage workerLoginStage = new Stage();
    new WorkerLogin().start(workerLoginStage);
  }

  public static void main(String[] args) {
    launch(args); // Launch JavaFX application
  }
}

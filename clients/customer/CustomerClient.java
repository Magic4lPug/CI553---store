package clients.customer;

import dbAccess.UserAccess;
import javafx.application.Application;
import javafx.stage.Stage;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomerClient extends Application {

  private static Stage customerWindow = null; // Singleton instance of the main window
  private static String currentUserID = null; // Track the currently logged-in user

  public static void launchWithUser(String userID) {
    if (customerWindow != null && customerWindow.isShowing()) {
      System.out.println("Customer window already open. Preventing duplicate.");
      return; // Prevent opening a new window if one already exists
    }

    currentUserID = userID; // Set the current user ID

    String stockURL = Names.STOCK_R;
    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);
    Connection databaseConnection = initializeDatabaseConnection();

    // Launch JavaFX Application Thread
    launchApplication(mrf, databaseConnection, userID);
  }

  public static void clearSession() {
    currentUserID = null; // Reset user ID
    if (customerWindow != null) {
      customerWindow.close();
      customerWindow = null;
      System.out.println("Customer window disposed and session cleared.");
    }
  }

  private static Connection initializeDatabaseConnection() {
    String dbURL = "jdbc:sqlite:catshop.db";
    try {
      Connection connection = DriverManager.getConnection(dbURL);
      System.out.println("Connected to database successfully.");
      return connection;
    } catch (SQLException e) {
      System.err.println("Failed to connect to database: " + e.getMessage());
      throw new RuntimeException("Database connection failed.");
    }
  }

  private static void launchApplication(MiddleFactory mf, Connection databaseConnection, String userID) {
    javafx.application.Platform.runLater(() -> {
      if (customerWindow == null) {
        customerWindow = new Stage(); // Initialise the JavaFX Stage
      }
      customerWindow.setTitle("Customer Client - The Treasure Trove");

      // Set up the MVC components
      CustomerModel model = new CustomerModel(mf);
      CustomerView view = new CustomerView();
      CustomerController cont = new CustomerController(model, view, databaseConnection, userID, customerWindow);
      view.setController(cont);

      model.addObserver(view); // Ensure the view observes the model
      customerWindow.setScene(view.getScene()); // Set the view scene
      customerWindow.show();
    });
  }

  @Override
  public void start(Stage primaryStage) {

    throw new UnsupportedOperationException("Use launchWithUser() to start the application.");
  }
}

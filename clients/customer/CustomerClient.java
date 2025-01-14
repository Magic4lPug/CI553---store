package clients.customer;

import dbAccess.UserAccess;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomerClient {

  private static JFrame customerWindow = null; // Singleton instance of the main window
  private static String currentUserID = null; // Track the currently logged-in user

  public static void launchWithUser(String userID) {
    if (customerWindow != null && customerWindow.isVisible()) {
      System.out.println("Customer window already open. Preventing duplicate.");
      return; // Prevent opening a new window if one already exists
    }

    currentUserID = userID; // Set the current user ID

    String stockURL = Names.STOCK_R;
    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);
    Connection databaseConnection = initializeDatabaseConnection();

    displayGUI(mrf, databaseConnection, userID);
  }

  public static void clearSession() {
    currentUserID = null; // Reset the current user ID
    if (customerWindow != null) {
      customerWindow.dispose(); // Ensure the window is disposed of
    }
    customerWindow = null; // Reset the singleton instance
    System.out.println("CustomerClient session cleared.");
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

  private static void displayGUI(MiddleFactory mf, Connection databaseConnection, String userID) {
    if (customerWindow == null) {
      customerWindow = new JFrame(); // Initialize only if null
    }
    customerWindow.setTitle("Customer Client (MVC RMI)");
    customerWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    CustomerModel model = new CustomerModel(mf);
    CustomerView view = new CustomerView(customerWindow, 0, 0);
    CustomerController cont = new CustomerController(model, view, databaseConnection, userID, customerWindow);
    view.setController(cont);

    model.addObserver(view);
    customerWindow.setVisible(true);
  }
}

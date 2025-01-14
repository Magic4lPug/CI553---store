package clients.customer;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The standalone Customer Client
 */
public class CustomerClient {
  public static void main(String[] args) {
    String stockURL = args.length < 1         // URL of stock R
            ? Names.STOCK_R                   // default location
            : args[0];                        // supplied location

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);
    Connection databaseConnection = initializeDatabaseConnection();

    String userID = "john_doe"; // Retrieve this dynamically based on login
    displayGUI(mrf, databaseConnection, userID);
  }

  private static Connection initializeDatabaseConnection() {
    String dbURL = "jdbc:sqlite:catshop.db"; // Replace with your database URL
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
    JFrame window = new JFrame();
    window.setTitle("Customer Client (MVC RMI)");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    CustomerModel model = new CustomerModel(mf);
    CustomerView view = new CustomerView(window, 0, 0);
    CustomerController cont = new CustomerController(model, view, databaseConnection, userID);
    view.setController(cont);

    model.addObserver(view); // Add observer to the model
    window.setVisible(true); // Display Screen
  }
}

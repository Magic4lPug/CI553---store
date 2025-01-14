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

  public static void launchWithUser(String userID) {
    String stockURL = Names.STOCK_R;

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);
    Connection databaseConnection = initializeDatabaseConnection();

    displayGUI(mrf, databaseConnection, userID);
  }

  public static void main(String[] args) {
    String stockURL = args.length < 1
            ? Names.STOCK_R
            : args[0];

    RemoteMiddleFactory mrf = new RemoteMiddleFactory();
    mrf.setStockRInfo(stockURL);
    Connection databaseConnection = initializeDatabaseConnection();

    String userID = loginAndGetUserID(databaseConnection); // Dynamically retrieve the userID
    if (userID != null) {
      displayGUI(mrf, databaseConnection, userID);
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

  private static String loginAndGetUserID(Connection databaseConnection) {
    String[] credentials = showLoginDialog();
    if (credentials == null) return null;

    try {
      UserAccess userAccess = new UserAccess(databaseConnection);
      boolean authenticated = userAccess.authenticate(credentials[0], credentials[1]);

      if (authenticated) {
        System.out.println("Login successful!");
        return credentials[0]; // Use username as userID
      } else {
        JOptionPane.showMessageDialog(null, "Invalid username or password.");
        return null;
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Login failed: " + e.getMessage());
      return null;
    }
  }

  private static String[] showLoginDialog() {
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    Object[] message = {
            "Username:", usernameField,
            "Password:", passwordField
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
    if (option == JOptionPane.OK_OPTION) {
      return new String[]{usernameField.getText(), new String(passwordField.getPassword())};
    } else {
      return null;
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

    model.addObserver(view);
    window.setVisible(true);
  }
}
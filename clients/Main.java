package clients;

import clients.login.CustomerLogin;
import clients.login.WorkerLogin;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      // Launch the customer and worker login windows
      startCustomerLogin();
      startWorkerLogin();
    });
  }

  /**
   * Launch the Customer Login window.
   */
  private static void startCustomerLogin() {
    CustomerLogin.main(new String[]{}); // Open the Customer Login window
  }

  /**
   * Launch the Worker Login window.
   */
  private static void startWorkerLogin() {
    WorkerLogin.main(new String[]{}); // Open the Worker Login window
  }
}

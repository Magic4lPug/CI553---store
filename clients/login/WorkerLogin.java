package clients.login;

import javax.swing.*;
import java.awt.*;

public class WorkerLogin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WorkerLogin::displayLogin);
    }

    private static void displayLogin() {
        JFrame frame = new JFrame("Worker Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new GridBagLayout());
        frame.setLocation(500, 100);

        JLabel passwordLabel = new JLabel("Worker Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());

            if (authenticateWorker(password)) {
                frame.dispose();
                clients.cashier.CashierClient.main(new String[]{}); // Open Worker Client
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid password.");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(loginButton, gbc);

        frame.setVisible(true);
    }

    private static boolean authenticateWorker(String password) {
        // Replace with your worker authentication logic
        return "worker123".equals(password);
    }
}

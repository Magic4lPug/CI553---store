package clients.login;

import javax.swing.*;
import java.awt.*;

public class CustomerLogin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerLogin::displayLogin);
    }

    private static void displayLogin() {
        JFrame frame = new JFrame("Customer Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridBagLayout());
        frame.setLocation(100, 100);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            System.out.println("Login button clicked.");
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticateCustomer(username, password)) {
                System.out.println("Authentication successful. Launching CustomerClient.");
                frame.dispose();
                clients.customer.CustomerClient.main(new String[]{}); // Open Customer Client
            } else {
                System.out.println("Authentication failed.");
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(usernameLabel, gbc);

        gbc.gridx = 1;
        frame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(loginButton, gbc);

        frame.setVisible(true);
    }

    private static boolean authenticateCustomer(String username, String password) {
        // Replace with your customer authentication logic
        return "customer1".equals(username) && "password1".equals(password);
    }
}

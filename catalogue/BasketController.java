package catalogue;

import clients.customer.CustomerModel;
import dbAccess.UserAccess;
import middle.SharedOrderQueue;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BasketController {
    private final Basket basket;
    private final BasketView basketView;
    private final Connection databaseConnection;
    private final String userID; // Unique user identifier for basket operations
    private CustomerModel customerModel; // Reference to CustomerModel

    public BasketController(Basket basket, Connection databaseConnection, String userID) {
        this.basket = basket;
        this.databaseConnection = databaseConnection;
        this.userID = userID; // Initialize userID
        this.basketView = new BasketView(basket, this, userID); // Pass userID to BasketView
        updateBasketView(); // Ensure the view is initialized with current basket contents
    }

    // Setter for CustomerModel to refresh product table after checkout
    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    // Show the basket view
    public void viewBasket() {
        basketView.updateBasketView(); // Refresh the view before showing it
        basketView.show();
    }

    public void saveBasket() {
        try {
            UserAccess userAccess = new UserAccess(databaseConnection);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(basket);
            oos.flush();
            byte[] basketData = bos.toByteArray();
            userAccess.saveBasketData(userID, basketData); // Save basket for the current user
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to save basket: " + e.getMessage());
        }
    }

    public void loadBasket() {
        try {
            UserAccess userAccess = new UserAccess(databaseConnection);
            byte[] basketData = userAccess.getBasketData(userID);
            if (basketData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(basketData);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Basket loadedBasket = (Basket) ois.readObject();
                basket.clear();
                basket.addAll(loadedBasket); // Load basket for the current user
                updateBasketView();
            } else {
                System.out.println("No saved basket found for this user.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to load basket: " + e.getMessage());
        }
    }

    public void checkoutBasket() {
        if (basket.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your basket is empty. Add items to checkout.");
        } else {
            try {
                databaseConnection.setAutoCommit(false);

                for (Product product : basket) {
                    updateStock(product); // Update stock in the database
                }

                SharedOrderQueue.addOrder(new Basket(basket)); // Send a copy of the basket to the cashier
                basket.clear(); // Clear the basket after checkout
                saveBasket(); // Update the database to reflect cleared basket
                databaseConnection.commit(); // Commit transaction

                JOptionPane.showMessageDialog(null, "Checkout successful! Your order has been sent to the cashier.");
                updateBasketView(); // Refresh the basket view

                // Refresh product table after checkout
                if (customerModel != null) {
                    customerModel.fetchAllProducts(); // Fetch updated products from the database
                    customerModel.markChanged(); // Use the new public method
                    customerModel.notifyObservers(); // Notify the view to refresh
                }

            } catch (Exception e) {
                try {
                    databaseConnection.rollback(); // Rollback transaction in case of error
                } catch (Exception rollbackEx) {
                    JOptionPane.showMessageDialog(null, "Error during rollback: " + rollbackEx.getMessage());
                }
                JOptionPane.showMessageDialog(null, "Checkout failed: " + e.getMessage());
            } finally {
                try {
                    databaseConnection.setAutoCommit(true); // Restore auto-commit mode
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Failed to restore auto-commit: " + e.getMessage());
                }
            }
        }
    }


    public void removeFromBasket(Product product) {
        if (basket.contains(product)) {
            basket.remove(product); // Remove the product from the basket
            saveBasket(); // Save the updated basket for the current user
            updateBasketView();
        }
    }

    private void updateStock(Product product) throws Exception {
        String query = "UPDATE StockTable SET stockLevel = stockLevel - ? WHERE productNo = ? AND stockLevel >= ?";
        try (PreparedStatement ps = databaseConnection.prepareStatement(query)) {
            ps.setInt(1, product.getQuantity());
            ps.setString(2, product.getProductNum());
            ps.setInt(3, product.getQuantity());

            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new Exception("Insufficient stock for product: " + product.getDescription());
            }
        }
    }

    public void updateBasketView() {
        basketView.updateBasketView(); // Refresh the basket view
    }
}

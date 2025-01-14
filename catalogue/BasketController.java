package catalogue;

import dbAccess.UserAccess;
import middle.SharedOrderQueue;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;

public class BasketController {
    private final Basket basket;
    private final BasketView basketView;
    private final Connection databaseConnection;
    private final String userID; // Add userID to the controller

    public BasketController(Basket basket, Connection databaseConnection, String userID) {
        this.basket = basket;
        this.databaseConnection = databaseConnection;
        this.userID = userID; // Initialize userID
        this.basketView = new BasketView(basket, this, userID); // Pass userID to BasketView
        updateBasketView(); // Ensure the view is initialized with current basket contents
    }

    // Show the basket view
    public void viewBasket() {
        basketView.updateBasketView(); // Refresh the view before showing it
        basketView.show();
    }

    // Save the basket to the database for the given user
    public void saveBasket() {
        try {
            UserAccess userAccess = new UserAccess(databaseConnection); // Use the provided database connection
            // Serialize the basket into bytes
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(basket);
            oos.flush();
            byte[] basketData = bos.toByteArray();

            // Save serialized data
            userAccess.saveBasketData(userID, basketData);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to save basket: " + e.getMessage());
        }
    }

    // Load the basket from the database for the given user
    public void loadBasket() {
        try {
            UserAccess userAccess = new UserAccess(databaseConnection); // Use the provided database connection
            byte[] basketData = userAccess.getBasketData(userID);

            if (basketData != null) {
                // Deserialize the basket data
                ByteArrayInputStream bis = new ByteArrayInputStream(basketData);
                ObjectInputStream ois = new ObjectInputStream(bis);
                Basket loadedBasket = (Basket) ois.readObject();

                // Update the basket
                basket.clear();
                basket.addAll(loadedBasket);
                updateBasketView();
            } else {
                JOptionPane.showMessageDialog(null, "No saved basket found for this user.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to load basket: " + e.getMessage());
        }
    }

    // Checkout the basket
    public void checkoutBasket() {
        if (basket.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your basket is empty. Add items to checkout.");
        } else {
            try {
                SharedOrderQueue.addOrder(new Basket(basket)); // Send a copy of the basket to the cashier
                basket.clear(); // Clear the basket after checkout
                saveBasket(); // Update the database to reflect the cleared basket
                JOptionPane.showMessageDialog(null, "Checkout successful! Your order has been sent to the cashier.");
                updateBasketView(); // Update the view after clearing the basket
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Checkout failed: " + e.getMessage());
            }
        }
    }

    // Remove an item from the basket and save changes
    public void removeFromBasket(Product product) {
        if (basket.contains(product)) {
            basket.remove(product);
            saveBasket(); // Save updated basket after removal
            updateBasketView();
        }
    }

    // Update the basket view
    public void updateBasketView() {
        basketView.updateBasketView(); // Refresh the table in BasketView
    }
}

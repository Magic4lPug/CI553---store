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
    private final String userID; // Unique user identifier for basket operations

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
                JOptionPane.showMessageDialog(null, "No saved basket found for this user.");
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
                SharedOrderQueue.addOrder(new Basket(basket)); // Send a copy of the basket to the cashier
                basket.clear(); // Clear the basket after checkout
                saveBasket(); // Update database to reflect cleared basket
                JOptionPane.showMessageDialog(null, "Checkout successful! Your order has been sent to the cashier.");
                updateBasketView();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Checkout failed: " + e.getMessage());
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

    public void updateBasketView() {
        basketView.updateBasketView(); // Refresh the basket view
    }
}

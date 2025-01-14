package catalogue;

import middle.SharedOrderQueue;

import javax.swing.JOptionPane;

public class BasketController {
    private final Basket basket;
    private final BasketView basketView;

    public BasketController(Basket basket) {
        this.basket = basket;
        this.basketView = new BasketView(basket, this);
        updateBasketView(); // Ensure the view is initialized with current basket contents
    }

    // Show the basket view
    public void viewBasket() {
        basketView.updateBasketView(); // Refresh the view before showing it
        basketView.show();
    }

    // Checkout the basket
    public void checkoutBasket() {
        if (basket.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your basket is empty. Add items to checkout.");
        } else {
            try {
                SharedOrderQueue.addOrder(new Basket(basket)); // Send a copy of the basket to the cashier
                basket.clear(); // Clear the basket after checkout
                JOptionPane.showMessageDialog(null, "Checkout successful! Your order has been sent to the cashier.");
                updateBasketView(); // Update the view after clearing the basket
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Checkout failed: " + e.getMessage());
            }
        }
    }

    // Update the basket view
    public void updateBasketView() {
        basketView.updateBasketView(); // Refresh the table in BasketView
    }
}

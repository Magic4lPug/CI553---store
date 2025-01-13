
package clients.cashier;

import javax.swing.*;

public class CashierController {
  private final CashierModel model;
  private final CashierView view;

  public CashierController(CashierModel model, CashierView view) {
    this.model = model;
    this.view = view;
  }

  public void processOrder() {
    if (model.getBasket() != null) {
      model.clearBasket();
      JOptionPane.showMessageDialog(null, "Order processed successfully!");
    } else {
      JOptionPane.showMessageDialog(null, "No orders to process.");
    }
  }

  // Add the refreshTasks method
  public void refreshTasks() {
    model.refreshTasks(); // Notify the model to refresh tasks
    JOptionPane.showMessageDialog(null, "Tasks refreshed successfully!");
  }

  public void claimTask(int taskIndex) {
    model.claimTask(taskIndex);
  }

  public void processClaimedTask(int taskIndex) {
    model.processClaimedTask(taskIndex);
  }

  public void completeProcessingTask(int taskIndex) {
    model.completeProcessingTask(taskIndex);
  }



}
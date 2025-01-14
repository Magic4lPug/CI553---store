package clients.cashier;

import catalogue.Basket;

import javax.swing.*;

public class CashierController {
  private final CashierModel model;
  private final CashierView view;

  public CashierController(CashierModel model, CashierView view) {
    this.model = model;
    this.view = view;
  }

  public void refreshTasks() {
    model.refreshTasks(); // Notify the model to refresh tasks
    JOptionPane.showMessageDialog(null, "Tasks refreshed successfully!");
  }

  public void claimTask(int taskIndex) {
    model.claimTask(taskIndex); // Move task from "Tasks" to "Processing"
  }

  public void completeProcessingTask(int taskIndex) {
    model.completeProcessingTask(taskIndex); // Move task from "Processing" to "Packed"
  }

  public void packTask(int taskIndex) {
    java.util.List<Basket> packedTasks = model.getPackedTasks(); // Access packed tasks via getter
    if (taskIndex >= 0 && taskIndex < packedTasks.size()) {
      Basket packedTask = packedTasks.get(taskIndex);
      packedTask.setPacked(true); // Mark task as packed
      model.refreshTasks(); // Notify observers to refresh UI
    } else {
      JOptionPane.showMessageDialog(null, "Invalid task selection.");
    }
  }
}

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
    model.refreshTasks();
    JOptionPane.showMessageDialog(null, "Tasks refreshed successfully!");
  }

  public void claimTask(int taskIndex) {
    model.claimTask(taskIndex);
  }

  public void completeProcessingTask(int taskIndex) {
    model.completeProcessingTask(taskIndex);
  }

  public void packTask(int taskIndex) {
    if (taskIndex >= 0) {
      model.packTask(taskIndex);
      System.out.println("Task successfully moved to completed orders!");
    } else {
      System.out.println("No task selected for packing!");
    }
  }

}

package clients.cashier;

import catalogue.Basket;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.SharedOrderQueue;
import middle.StockReadWriter;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CashierModel extends Observable {
  private enum State {PROCESS, CHECKED}

  private State theState = State.PROCESS; // Current state
  private Basket theBasket = null;        // Current customer order
  private final List<Basket> taskList = new ArrayList<>(); // Unclaimed tasks
  private final List<Basket> claimedTasks = new ArrayList<>(); // Claimed tasks
  private final List<Basket> processingTasks = new ArrayList<>(); // Processing tasks
  private final List<Basket> packedTasks = new ArrayList<>(); // Packed tasks

  private StockReadWriter theStock = null;
  private OrderProcessing theOrder = null;

  public CashierModel(MiddleFactory mf) {
    try {
      theStock = mf.makeStockReadWriter(); // Fetch stock details
      theOrder = mf.makeOrderProcessing(); // Fetch order processing
      startOrderListening();              // Start listening for orders
    } catch (Exception e) {
      System.err.println("Error initializing CashierModel: " + e.getMessage());
    }
  }

  private void startOrderListening() {
    new Thread(this::listenForOrders).start();
  }

  private void listenForOrders() {
    try {
      while (true) {
        Basket incomingBasket = SharedOrderQueue.getOrder(); // Block until an order is available
        taskList.add(incomingBasket); // Add to task list
        setChanged();
        notifyObservers("New order received!");
      }
    } catch (InterruptedException e) {
      DEBUG.error("CashierModel.listenForOrders interrupted: %s", e.getMessage());
    }
  }

  public Basket getBasket() {
    return theBasket;
  }

  public void clearBasket() {
    this.theBasket = null;
    setChanged();
    notifyObservers("Order processed. Ready for the next one.");
  }

  public DefaultTableModel getTaskData() {
    return createTableModel(taskList, "Unclaimed");
  }

  public DefaultTableModel getClaimedTaskData() {
    return createTableModel(claimedTasks, "Claimed");
  }

  public DefaultTableModel getProcessingTaskData() {
    return createTableModel(processingTasks, "Processing");
  }

  public DefaultTableModel getPackedTaskData() {
    return createTableModel(packedTasks, "Packed");
  }

  private DefaultTableModel createTableModel(List<Basket> list, String status) {
    String[] columns = {"Order ID", "Items", "Total Price", "Status"};
    Object[][] data = new Object[list.size()][columns.length];

    for (int i = 0; i < list.size(); i++) {
      Basket basket = list.get(i);
      double total = basket.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
      data[i][0] = basket.getOrderNum();
      data[i][1] = basket.size(); // Number of items
      data[i][2] = total;         // Total price
      data[i][3] = status;        // Status
    }

    return new DefaultTableModel(data, columns);
  }

  public void claimTask(int taskIndex) {
    if (taskIndex >= 0 && taskIndex < taskList.size()) {
      Basket claimedTask = taskList.remove(taskIndex); // Remove from unclaimed tasks
      claimedTasks.add(claimedTask);                  // Add to claimed tasks
      setChanged();
      notifyObservers("Task claimed successfully!");
    } else {
      setChanged();
      notifyObservers("Invalid task selection.");
    }
  }

  public void processClaimedTask(int taskIndex) {
    if (taskIndex >= 0 && taskIndex < claimedTasks.size()) {
      Basket processingTask = claimedTasks.remove(taskIndex); // Remove from claimed tasks
      processingTasks.add(processingTask);                   // Add to processing tasks
      setChanged();
      notifyObservers("Task moved to processing!");
    } else {
      setChanged();
      notifyObservers("Invalid task selection.");
    }
  }

  public void completeProcessingTask(int taskIndex) {
    if (taskIndex >= 0 && taskIndex < processingTasks.size()) {
      Basket packedTask = processingTasks.remove(taskIndex); // Remove from processing tasks
      packedTasks.add(packedTask);                          // Add to packed tasks
      setChanged();
      notifyObservers("Task packed and ready for shipping!");
    } else {
      setChanged();
      notifyObservers("Invalid task selection.");
    }
  }

  public void refreshTasks() {
    setChanged();
    notifyObservers("Tasks refreshed.");
  }

  public void askForUpdate() {
    setChanged();
    notifyObservers("Welcome! Waiting for orders.");
  }
}

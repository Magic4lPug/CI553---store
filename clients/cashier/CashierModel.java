// Updated CashierModel
package clients.cashier;

import catalogue.Basket;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.SharedOrderQueue;
import middle.StockReadWriter;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CashierModel extends Observable {
  private Basket theBasket = null;
  private final List<Basket> taskList = new ArrayList<>();
  private final List<Basket> processingTasks = new ArrayList<>();
  private final List<Basket> packedTasks = new ArrayList<>();
  private Connection databaseConnection;
  private StockReadWriter theStock = null;
  private OrderProcessing theOrder = null;

  public CashierModel(MiddleFactory mf) {
    try {
      theStock = mf.makeStockReadWriter();
      theOrder = mf.makeOrderProcessing();
      databaseConnection = DriverManager.getConnection("jdbc:sqlite:catshop.db");
      loadTasksFromDatabase();
      startOrderListening();
    } catch (Exception e) {
      System.err.println("Error initializing CashierModel: " + e.getMessage());
    }
  }

  private void startOrderListening() {
    new Thread(this::listenForOrders).start();
  }

  public void listenForOrders() {
    try {
      while (true) {
        Basket incomingBasket = SharedOrderQueue.getOrder();
        taskList.add(incomingBasket);
        saveNewTask(incomingBasket, "Unclaimed");
        setChanged();
        notifyObservers("New order received!");
      }
    } catch (InterruptedException e) {
      DEBUG.error("CashierModel.listenForOrders interrupted: %s", e.getMessage());
    }
  }

  private void saveNewTask(Basket task, String status) {
    try (PreparedStatement ps = databaseConnection.prepareStatement(
            "INSERT INTO TaskTable (taskData, status) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
      byte[] serializedBasket = serializeBasket(task);
      ps.setBytes(1, serializedBasket);
      ps.setString(2, status);
      ps.executeUpdate();

      try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) {
          int taskID = keys.getInt(1);
          task.setOrderNum(taskID);
        }
      }

      System.out.println("Task saved to database: " + task.getOrderNum());
    } catch (SQLException | IOException e) {
      System.err.println("Failed to save new task: " + e.getMessage());
    }
  }

  private void updateTaskStatus(int taskID, String status) {
    try (PreparedStatement ps = databaseConnection.prepareStatement(
            "UPDATE TaskTable SET status = ? WHERE taskID = ?")) {
      ps.setString(1, status);
      ps.setInt(2, taskID);
      ps.executeUpdate();
      System.out.println("Task updated to status: " + status);
    } catch (SQLException e) {
      System.err.println("Failed to update task status: " + e.getMessage());
    }
  }

  private byte[] serializeBasket(Basket basket) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(basket);
      return bos.toByteArray();
    }
  }

  private Basket deserializeBasket(byte[] data) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
         ObjectInputStream ois = new ObjectInputStream(bis)) {
      return (Basket) ois.readObject();
    }
  }

  private void loadTasksFromDatabase() {
    try (Statement stmt = databaseConnection.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM TaskTable")) {
      while (rs.next()) {
        byte[] taskData = rs.getBytes("taskData");
        String status = rs.getString("status");
        int taskID = rs.getInt("taskID");
        Basket task = deserializeBasket(taskData);
        task.setOrderNum(taskID);

        switch (status) {
          case "Unclaimed" -> taskList.add(task);
          case "Processing" -> processingTasks.add(task);
          case "Packed" -> packedTasks.add(task);
        }
      }
      System.out.println("Tasks loaded from database.");
    } catch (SQLException | IOException | ClassNotFoundException e) {
      System.err.println("Failed to load tasks from database: " + e.getMessage());
    }
  }

  public DefaultTableModel getTaskData() {
    return createTableModel(taskList, "Unclaimed");
  }

  public DefaultTableModel getProcessingTaskData() {
    return createTableModel(processingTasks, "Processing");
  }

  public DefaultTableModel getPackedTaskData() {
    return createTableModel(packedTasks, "Packed");
  }

  private DefaultTableModel createTableModel(List<Basket> list, String defaultStatus) {
    String[] columns = {"Order ID", "Items", "Total Price", "Status"};
    Object[][] data = new Object[list.size()][columns.length];

    for (int i = 0; i < list.size(); i++) {
      Basket basket = list.get(i);
      double total = basket.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
      data[i][0] = basket.getOrderNum();
      data[i][1] = basket.size();
      data[i][2] = total;
      data[i][3] = defaultStatus;
    }

    return new DefaultTableModel(data, columns) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
  }

  public void claimTask(int taskIndex) {
    if (taskIndex >= 0 && taskIndex < taskList.size()) {
      Basket claimedTask = taskList.remove(taskIndex);
      processingTasks.add(claimedTask);
      updateTaskStatus(claimedTask.getOrderNum(), "Processing");
      setChanged();
      notifyObservers();
    }
  }

  public void completeProcessingTask(int taskIndex) {
    if (taskIndex >= 0 && taskIndex < processingTasks.size()) {
      Basket packedTask = processingTasks.remove(taskIndex);
      packedTasks.add(packedTask);
      updateTaskStatus(packedTask.getOrderNum(), "Packed");
      setChanged();
      notifyObservers();
    }
  }

  public void packTask(int taskIndex) {
    if (taskIndex >= 0 && taskIndex < packedTasks.size()) {
      Basket completedTask = packedTasks.remove(taskIndex);
      updateTaskStatus(completedTask.getOrderNum(), "Completed"); // Update to Completed
      setChanged();
      notifyObservers("Task completed and moved to Completed Orders."); // Notify
    }
  }


  public void refreshTasks() {
    setChanged();
    notifyObservers();
  }

  public void askForUpdate() {
    setChanged();
    notifyObservers();
  }
}
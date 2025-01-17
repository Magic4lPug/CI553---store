package clients.cashier;

import clients.backDoor.BackDoorController;
import clients.backDoor.BackDoorModel;
import clients.backDoor.BackDoorView;
import middle.MiddleFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CashierView implements Observer {
  private static final int H = 500; // Height of window pixels
  private static final int W = 600; // Width of window pixels

  private final JTabbedPane tabbedPane = new JTabbedPane();
  private final JTable taskTable = new JTable();
  private final JScrollPane taskScrollPane = new JScrollPane(taskTable);

  private final JTable processingTaskTable = new JTable();
  private final JScrollPane processingTaskScrollPane = new JScrollPane(processingTaskTable);
  private final JButton completeTaskButton = new JButton("Complete Task");

  private final JTable packedTaskTable = new JTable();
  private final JScrollPane packedTaskScrollPane = new JScrollPane(packedTaskTable);
  private final JButton packTaskButton = new JButton("Pack Task");

  private final JTable completedOrdersTable = new JTable();
  private final JScrollPane completedOrdersScrollPane = new JScrollPane(completedOrdersTable);
  private final JTextField searchBox = new JTextField();
  private final JButton searchButton = new JButton("Search");

  private final JLabel theOutput = new JLabel("Status: Ready");
  private CashierController cont;
  private final CompletedOrdersModel completedOrdersModel;

  public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    Container cp = rpc.getContentPane();
    Container rootWindow = (Container) rpc;
    cp.setLayout(null);
    rootWindow.setSize(W, H);
    rootWindow.setLocation(x, y);

    completedOrdersModel = new CompletedOrdersModel(mf); // Initialise CompletedOrdersModel

    // Tab 1: Tasks
    JPanel taskPanel = new JPanel(null);
    taskPanel.setBackground(Color.DARK_GRAY);
    taskScrollPane.setBounds(10, 10, 560, 300);
    taskPanel.add(taskScrollPane);

    JButton claimTaskButton = new JButton("Claim Task");
    claimTaskButton.setBounds(10, 320, 150, 30);
    styleButton(claimTaskButton);
    claimTaskButton.addActionListener(e -> {
      int selectedRow = taskTable.getSelectedRow();
      if (selectedRow != -1) {
        cont.claimTask(selectedRow);
      } else {
        JOptionPane.showMessageDialog(null, "No task selected!");
      }
    });
    taskPanel.add(claimTaskButton);

    tabbedPane.addTab("Tasks", taskPanel);

    // Tab 2: Processing Tasks
    JPanel processingTaskPanel = new JPanel(null);
    processingTaskPanel.setBackground(Color.DARK_GRAY);
    processingTaskScrollPane.setBounds(10, 10, 560, 300);
    processingTaskPanel.add(processingTaskScrollPane);

    completeTaskButton.setBounds(10, 320, 150, 30);
    styleButton(completeTaskButton);
    completeTaskButton.addActionListener(e -> {
      int selectedRow = processingTaskTable.getSelectedRow();
      if (selectedRow != -1) {
        cont.completeProcessingTask(selectedRow);
      } else {
        JOptionPane.showMessageDialog(null, "No processing task selected!");
      }
    });
    processingTaskPanel.add(completeTaskButton);

    tabbedPane.addTab("Processing Tasks", processingTaskPanel);

    // Tab 3: Packed Tasks
    JPanel packedTaskPanel = new JPanel(null);
    packedTaskPanel.setBackground(Color.DARK_GRAY);
    packedTaskScrollPane.setBounds(10, 10, 560, 300);
    packedTaskPanel.add(packedTaskScrollPane);

    packTaskButton.setBounds(10, 320, 150, 30);
    styleButton(packTaskButton);
    packTaskButton.addActionListener(e -> {
      int selectedRow = packedTaskTable.getSelectedRow();
      if (selectedRow != -1) {
        cont.packTask(selectedRow);
        JOptionPane.showMessageDialog(null, "Task successfully packed!");
      } else {
        JOptionPane.showMessageDialog(null, "No task selected for packing!");
      }
    });
    packedTaskPanel.add(packTaskButton);

    tabbedPane.addTab("Packed Tasks", packedTaskPanel);

    // Tab 4: Completed Orders
    JPanel completedOrdersPanel = new JPanel(null);
    completedOrdersPanel.setBackground(Color.DARK_GRAY);
    completedOrdersScrollPane.setBounds(10, 50, 560, 260);
    completedOrdersPanel.add(completedOrdersScrollPane);

    searchBox.setBounds(10, 10, 200, 30);
    styleTextField(searchBox);
    completedOrdersPanel.add(searchBox);

    searchButton.setBounds(220, 10, 100, 30);
    styleButton(searchButton);
    searchButton.addActionListener(e -> {
      String orderID = searchBox.getText().trim();
      if (!orderID.isEmpty()) {
        try {
          int id = Integer.parseInt(orderID);
          DefaultTableModel model = completedOrdersModel.searchCompletedOrder(id);
          completedOrdersTable.setModel(model);
        } catch (NumberFormatException ex) {
          JOptionPane.showMessageDialog(null, "Invalid Order ID format.");
        }
      } else {
        completedOrdersTable.setModel(completedOrdersModel.getCompletedOrdersData());
      }
    });
    completedOrdersPanel.add(searchButton);

    tabbedPane.addTab("Completed Orders", completedOrdersPanel);

    // Tab 5: Stock Management (BackDoorView)
    try {
      BackDoorModel backDoorModel = new BackDoorModel(mf.makeStockReadWriter());
      BackDoorView backDoorView = new BackDoorView(mf);
      BackDoorController backDoorController = new BackDoorController(backDoorModel, backDoorView);
      backDoorView.setController(backDoorController);
      backDoorModel.addObserver(backDoorView);

      JPanel stockManagementPanel = backDoorView.getPanel();
      stockManagementPanel.setBackground(Color.DARK_GRAY);
      tabbedPane.addTab("Stock Management", stockManagementPanel); // Add Stock Management as a tab
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Error initializing Stock Management tab: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    tabbedPane.setBounds(0, 0, W, H);
    tabbedPane.setBackground(Color.BLACK);
    tabbedPane.setForeground(Color.YELLOW);
    cp.add(tabbedPane);

    rootWindow.setVisible(true);

    completedOrdersTable.setModel(completedOrdersModel.getCompletedOrdersData());
  }

  private void styleButton(JButton button) {
    button.setBackground(Color.BLACK);
    button.setForeground(Color.YELLOW);
    button.setFont(new Font("Arial", Font.BOLD, 12));
  }

  private void styleTextField(JTextField textField) {
    textField.setBackground(Color.BLACK);
    textField.setForeground(Color.YELLOW);
    textField.setCaretColor(Color.YELLOW);
    textField.setFont(new Font("Arial", Font.PLAIN, 12));
  }

  public void setController(CashierController c) {
    cont = c;
  }

  @Override
  public void update(Observable modelC, Object arg) {
    CashierModel model = (CashierModel) modelC;
    String message = (String) arg;
    theOutput.setText("Status: " + message);

    // Update task tables
    taskTable.setModel(model.getTaskData());
    processingTaskTable.setModel(model.getProcessingTaskData());
    packedTaskTable.setModel(model.getPackedTaskData());

    // Refresh the completed orders table automatically
    completedOrdersTable.setModel(completedOrdersModel.getCompletedOrdersData());
  }
}

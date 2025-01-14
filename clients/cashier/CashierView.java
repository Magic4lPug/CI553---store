package clients.cashier;

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
  private final JTable taskTable = new JTable(); // Task table
  private final JScrollPane taskScrollPane = new JScrollPane(taskTable);

  private final JTable processingTaskTable = new JTable(); // Table for processing tasks
  private final JScrollPane processingTaskScrollPane = new JScrollPane(processingTaskTable);
  private final JButton completeTaskButton = new JButton("Complete Task");

  private final JTable packedTaskTable = new JTable(); // Table for packed tasks
  private final JScrollPane packedTaskScrollPane = new JScrollPane(packedTaskTable);
  private final JButton packTaskButton = new JButton("Pack Task"); // New "Pack Task" button

  private final JLabel theOutput = new JLabel("Status: Ready"); // Added status output
  private CashierController cont;

  public CashierView(RootPaneContainer rpc, MiddleFactory mf, int x, int y) {
    Container cp = rpc.getContentPane();
    Container rootWindow = (Container) rpc;
    cp.setLayout(null);
    rootWindow.setSize(W, H);
    rootWindow.setLocation(x, y);

    // Tab 1: Tasks
    JPanel taskPanel = new JPanel(null);
    taskScrollPane.setBounds(10, 10, 560, 300);
    taskPanel.add(taskScrollPane);

    JButton claimTaskButton = new JButton("Claim Task");
    claimTaskButton.setBounds(10, 320, 150, 30);
    claimTaskButton.addActionListener(e -> {
      int selectedRow = taskTable.getSelectedRow();
      if (selectedRow != -1) {
        cont.claimTask(selectedRow); // Claim the selected task
      } else {
        JOptionPane.showMessageDialog(null, "No task selected!");
      }
    });
    taskPanel.add(claimTaskButton);

    tabbedPane.addTab("Tasks", taskPanel);

    // Tab 2: Processing Tasks
    JPanel processingTaskPanel = new JPanel(null);
    processingTaskScrollPane.setBounds(10, 10, 560, 300);
    processingTaskPanel.add(processingTaskScrollPane);

    completeTaskButton.setBounds(10, 320, 150, 30);
    completeTaskButton.addActionListener(e -> {
      int selectedRow = processingTaskTable.getSelectedRow();
      if (selectedRow != -1) {
        cont.completeProcessingTask(selectedRow); // Complete processing task
      } else {
        JOptionPane.showMessageDialog(null, "No processing task selected!");
      }
    });
    processingTaskPanel.add(completeTaskButton);

    tabbedPane.addTab("Processing Tasks", processingTaskPanel);

    // Tab 3: Packed Tasks
    JPanel packedTaskPanel = new JPanel(null);
    packedTaskScrollPane.setBounds(10, 10, 560, 300);
    packedTaskPanel.add(packedTaskScrollPane);

    packTaskButton.setBounds(10, 320, 150, 30);
    packTaskButton.addActionListener(e -> {
      int selectedRow = packedTaskTable.getSelectedRow();
      if (selectedRow != -1) {
        cont.packTask(selectedRow); // Mark task as packed
        JOptionPane.showMessageDialog(null, "Task successfully packed!");
      } else {
        JOptionPane.showMessageDialog(null, "No task selected for packing!");
      }
    });
    packedTaskPanel.add(packTaskButton);

    tabbedPane.addTab("Packed Tasks", packedTaskPanel);


    // Tab 4: Backdoor
    BackDoorView backDoorView = new BackDoorView(mf);
    tabbedPane.addTab("Backdoor", backDoorView.getPanel());

    tabbedPane.setBounds(0, 0, W, H);
    cp.add(tabbedPane);

    rootWindow.setVisible(true);
  }

  public void setController(CashierController c) {
    cont = c;
  }

  @Override
  public void update(Observable modelC, Object arg) {
    CashierModel model = (CashierModel) modelC;
    String message = (String) arg;
    theOutput.setText("Status: " + message); // Update status message

    // Update task table
    DefaultTableModel taskTableModel = model.getTaskData();
    if (taskTableModel != null) {
      taskTable.setModel(taskTableModel);
    }

    // Update processing task table
    DefaultTableModel processingTableModel = model.getProcessingTaskData();
    if (processingTableModel != null) {
      processingTaskTable.setModel(processingTableModel);
    }

    // Update packed task table
    DefaultTableModel packedTableModel = model.getPackedTaskData();
    if (packedTableModel != null) {
      packedTaskTable.setModel(packedTableModel);
    }
  }
}

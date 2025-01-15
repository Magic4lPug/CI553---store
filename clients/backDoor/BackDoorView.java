package clients.backDoor;

import middle.MiddleFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the BackDoor view for staff stock management.
 */
public class BackDoorView implements Observer {
  private static final String QUERY = "Query";
  private static final String RESTOCK = "Restock";
  private static final String REMOVE = "Remove";

  private final JPanel panel = new JPanel(null); // Panel to hold the UI
  private final JLabel pageTitle = new JLabel("Stock Management");
  private final JLabel actionStatus = new JLabel(); // Label for displaying status messages
  private final PlaceholderTextField productInput = new PlaceholderTextField("Enter product number");
  private final PlaceholderTextField quantityInput = new PlaceholderTextField("Enter quantity (for restock/remove)");
  private final JTextArea outputArea = new JTextArea();
  private final JScrollPane scrollPane = new JScrollPane(outputArea);
  private final JButton queryButton = new JButton(QUERY);
  private final JButton restockButton = new JButton(RESTOCK);
  private final JButton removeButton = new JButton(REMOVE);

  private BackDoorController controller;

  public BackDoorView(MiddleFactory mf) {
    initializeUI();
  }

  private void initializeUI() {
    Font font = new Font("Monospaced", Font.PLAIN, 12);

    // Page title
    pageTitle.setBounds(100, 10, 200, 20);
    pageTitle.setFont(new Font("Arial", Font.BOLD, 16));
    panel.add(pageTitle);

    // Query button
    queryButton.setBounds(20, 50, 100, 40);
    queryButton.addActionListener(e -> {
      if (controller != null) {
        controller.doQuery(productInput.getText());
      } else {
        showError("Controller not set!");
      }
    });
    panel.add(queryButton);

    // Restock button
    restockButton.setBounds(20, 100, 100, 40);
    restockButton.addActionListener(e -> {
      if (controller != null) {
        controller.doRestock(productInput.getText(), quantityInput.getText());
      } else {
        showError("Controller not set!");
      }
    });
    panel.add(restockButton);

    // Remove button
    removeButton.setBounds(20, 150, 100, 40);
    removeButton.addActionListener(e -> {
      if (controller != null) {
        controller.doRemoveStock(productInput.getText(), quantityInput.getText());
      } else {
        showError("Controller not set!");
      }
    });
    panel.add(removeButton);

    // Product input field
    productInput.setBounds(150, 50, 200, 40);
    panel.add(productInput);

    // Quantity input field
    quantityInput.setBounds(150, 100, 200, 40);
    panel.add(quantityInput);

    // Output area (scroll pane)
    scrollPane.setBounds(150, 150, 200, 100);
    outputArea.setFont(font);
    outputArea.setEditable(false); // Read-only
    panel.add(scrollPane);

    // Action status label
    actionStatus.setBounds(20, 260, 350, 20);
    actionStatus.setForeground(Color.RED); // Error messages in red
    panel.add(actionStatus);
  }

  public JPanel getPanel() {
    return panel;
  }

  public void setController(BackDoorController controller) {
    this.controller = controller;
  }

  @Override
  public void update(Observable model, Object arg) {
    if (arg instanceof String) {
      outputArea.setText((String) arg); // Show the action message
      actionStatus.setText(""); // Clear error messages
    } else {
      showError("Unexpected data from model.");
    }
  }

  private void showError(String message) {
    actionStatus.setText(message); // Display error messages
    outputArea.setText(""); // Clear the output area
  }

  /**
   * A custom JTextField implementation that supports placeholder text.
   */
  private static class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField(String placeholder) {
      this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (getText().isEmpty() && !isFocusOwner()) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.GRAY);
        g2.setFont(getFont().deriveFont(Font.ITALIC));
        g2.drawString(placeholder, 5, getHeight() / 2 + g2.getFontMetrics().getAscent() / 2 - 2);
        g2.dispose();
      }
    }

    public String getPlaceholder() {
      return placeholder;
    }

    public void setPlaceholder(String placeholder) {
      this.placeholder = placeholder;
      repaint();
    }
  }
}

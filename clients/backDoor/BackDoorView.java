package clients.backDoor;

import middle.MiddleFactory;
import middle.StockReadWriter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Customer view.
 */

public class BackDoorView implements Observer {
  private static final String RESTOCK = "Add";
  private static final String CLEAR = "Clear";
  private static final String QUERY = "Query";

  private static final int H = 300; // Height of window pixels
  private static final int W = 400; // Width of window pixels

  private final JPanel panel = new JPanel(null); // Panel to hold the UI
  private final JLabel pageTitle = new JLabel();
  private final JLabel theAction = new JLabel();
  private final JTextField theInput = new JTextField();
  private final JTextField theInputNo = new JTextField();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtClear = new JButton(CLEAR);
  private final JButton theBtRStock = new JButton(RESTOCK);
  private final JButton theBtQuery = new JButton(QUERY);

  private StockReadWriter theStock = null;
  private BackDoorController cont = null;

  public BackDoorView(MiddleFactory mf) {
    try {
      theStock = mf.makeStockReadWriter(); // Database access
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }

    initializeUI();
  }

  private void initializeUI() {
    Font f = new Font("Monospaced", Font.PLAIN, 12); // Font

    pageTitle.setBounds(110, 0, 270, 20);
    pageTitle.setText("Staff check and manage stock");
    panel.add(pageTitle);

    theBtQuery.setBounds(16, 25 + 60 * 0, 80, 40); // Query button
    theBtQuery.addActionListener(e -> cont.doQuery(theInput.getText()));
    panel.add(theBtQuery);

    theBtRStock.setBounds(16, 25 + 60 * 1, 80, 40); // Restock button
    theBtRStock.addActionListener(e -> cont.doRStock(theInput.getText(), theInputNo.getText()));
    panel.add(theBtRStock);

    theBtClear.setBounds(16, 25 + 60 * 2, 80, 40); // Clear button
    theBtClear.addActionListener(e -> cont.doClear());
    panel.add(theBtClear);

    theAction.setBounds(110, 25, 270, 20); // Message area
    theAction.setText("");
    panel.add(theAction);

    theInput.setBounds(110, 50, 120, 40); // Input area
    theInput.setText("");
    panel.add(theInput);

    theInputNo.setBounds(260, 50, 120, 40); // Input area for quantity
    theInputNo.setText("0");
    panel.add(theInputNo);

    theSP.setBounds(110, 100, 270, 160); // Scrolling pane
    theOutput.setText("");
    theOutput.setFont(f);
    theSP.getViewport().add(theOutput);
    panel.add(theSP);
  }

  public JPanel getPanel() {
    return panel;
  }

  public void setController(BackDoorController c) {
    cont = c;
  }

  @Override
  public void update(Observable modelC, Object arg) {
    BackDoorModel model = (BackDoorModel) modelC;
    String message = (String) arg;
    theAction.setText(message);

    theOutput.setText(model.getBasket().getDetails());
    theInput.requestFocus();
  }
}

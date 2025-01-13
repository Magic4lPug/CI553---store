package clients.packing;

import catalogue.Basket;
import middle.MiddleFactory;
import middle.OrderProcessing;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the Packing view.
 */
public class PackingView implements Observer {
  private static final String PACKED = "Packed";

  private static final int H = 300; // Height of window pixels
  private static final int W = 400; // Width of window pixels

  private final JPanel panel = new JPanel(null); // Panel to hold the UI
  private final JLabel pageTitle = new JLabel();
  private final JLabel theAction = new JLabel();
  private final JTextArea theOutput = new JTextArea();
  private final JScrollPane theSP = new JScrollPane();
  private final JButton theBtPack = new JButton(PACKED);

  private OrderProcessing theOrder = null;
  private PackingController cont = null;

  public PackingView(MiddleFactory mf) {
    try {
      theOrder = mf.makeOrderProcessing(); // Process order
    } catch (Exception e) {
      System.out.println("Exception: " + e.getMessage());
    }

    initializeUI();
  }

  private void initializeUI() {
    Font f = new Font("Monospaced", Font.PLAIN, 12); // Font

    pageTitle.setBounds(110, 0, 270, 20);
    pageTitle.setText("Packing Bought Order");
    panel.add(pageTitle);

    theBtPack.setBounds(16, 25 + 60 * 0, 80, 40); // Packed button
    theBtPack.addActionListener(e -> cont.doPacked());
    panel.add(theBtPack);

    theAction.setBounds(110, 25, 270, 20); // Message area
    theAction.setText("");
    panel.add(theAction);

    theSP.setBounds(110, 55, 270, 205); // Scrolling pane
    theOutput.setText("");
    theOutput.setFont(f);
    theSP.getViewport().add(theOutput);
    panel.add(theSP);
  }

  public JPanel getPanel() {
    return panel;
  }

  public void setController(PackingController c) {
    cont = c;
  }

  @Override
  public void update(Observable modelC, Object arg) {
    PackingModel model = (PackingModel) modelC;
    String message = (String) arg;
    theAction.setText(message);

    Basket basket = model.getBasket();
    if (basket != null) {
      theOutput.setText(basket.getDetails());
    } else {
      theOutput.setText("");
    }
  }
}

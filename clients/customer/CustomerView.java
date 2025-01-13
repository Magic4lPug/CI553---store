package clients.customer;

import catalogue.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Implements the updated Customer view.
 */
public class CustomerView implements Observer {
  private static final int H = 500; // Height of window pixels
  private static final int W = 600; // Width of window pixels

  private final JLabel pageTitle = new JLabel("Available Products");
  private final JTextField searchField = new JTextField(20);
  private final JButton searchButton = new JButton("Search");
  private final JTable productTable = new JTable(); // Product list
  private final JScrollPane productScrollPane = new JScrollPane(productTable);
  private final JButton addToBasketButton = new JButton("Add to Basket");
  private final JButton basketButton = new JButton("🛒 Basket");
  private final JLabel theAction = new JLabel();
  private final JButton checkoutButton = new JButton("Checkout");

  private CustomerController cont;

  public CustomerView(RootPaneContainer rpc, int x, int y) {
    Container cp = rpc.getContentPane();
    Container rootWindow = (Container) rpc;
    cp.setLayout(null);
    rootWindow.setSize(W, H);
    rootWindow.setLocation(x, y);

    pageTitle.setBounds(20, 10, 200, 20);
    cp.add(pageTitle);

    searchField.setBounds(20, 40, 300, 30);
    cp.add(searchField);

    searchButton.setBounds(330, 40, 100, 30);
    searchButton.addActionListener(e -> cont.doSearch(searchField.getText()));
    cp.add(searchButton);

    basketButton.setBounds(500, 10, 80, 30);
    basketButton.addActionListener(e -> cont.viewBasket());
    cp.add(basketButton);

    productScrollPane.setBounds(20, 80, 560, 300);
    cp.add(productScrollPane);

    theAction.setBounds(20, 390, 560, 30);
    cp.add(theAction);

    addToBasketButton.setBounds(20, 400, 150, 30);
    addToBasketButton.addActionListener(e -> cont.addToBasket(getSelectedProduct()));
    cp.add(addToBasketButton);

    checkoutButton.setBounds(200, 400, 150, 30);
    checkoutButton.addActionListener(e -> cont.checkoutBasket());
    cp.add(checkoutButton);

    rootWindow.setVisible(true);;
  }

  public void setController(CustomerController c) {
    cont = c;
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof CustomerModel) {
      CustomerModel model = (CustomerModel) o;

      if (model.hasError()) {
        showError(model.getErrorMessage());
      } else {
        populateProductTable(model.getProducts());
      }
    }
  }

  private void showError(String errorMessage) {
    JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public void populateProductTable(List<Product> products) {
    DefaultTableModel tableModel = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // Disable cell editing
      }
    };

    tableModel.addColumn("Product No");
    tableModel.addColumn("Description");
    tableModel.addColumn("Price");
    tableModel.addColumn("Stock Level");

    for (Product product : products) {
      tableModel.addRow(new Object[]{
              product.getProductNum(),
              product.getDescription(),
              product.getPrice(),
              product.getQuantity()
      });
    }

    productTable.setModel(tableModel);
  }

  private Product getSelectedProduct() {
    int selectedRow = productTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(null, "Please select a product first.");
      return null;
    }

    String productNum = (String) productTable.getValueAt(selectedRow, 0);
    String description = (String) productTable.getValueAt(selectedRow, 1);
    double price = (double) productTable.getValueAt(selectedRow, 2);
    int quantity = (int) productTable.getValueAt(selectedRow, 3);

    return new Product(productNum, description, price, quantity);
  }

}

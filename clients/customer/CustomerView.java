package clients.customer;

import catalogue.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CustomerView implements Observer {
    private static final int H = 550; // Height of window pixels
    private static final int W = 700; // Width of window pixels

    private final JLabel pageTitle = new JLabel("Available Products");
    private final JTextField searchField = new JTextField(20);
    private final JButton searchButton = new JButton("Search");
    private final JTable productTable = new JTable(); // Product list
    private final JScrollPane productScrollPane = new JScrollPane(productTable);
    private final JButton addToBasketButton = new JButton("Add to Basket");
    private final JButton basketButton = new JButton("🛒 Basket");
    private final JLabel theAction = new JLabel();
    private final JButton profileButton = new JButton("Profile"); // New Profile button

    private CustomerController cont;

    public CustomerView(RootPaneContainer rpc, int x, int y) {
        Container cp = rpc.getContentPane();
        Container rootWindow = (Container) rpc;
        cp.setLayout(null);
        rootWindow.setSize(W, H);
        rootWindow.setLocation(x, y);

        // Profile Button - Top left
        profileButton.setBounds(20, 10, 100, 30); // Larger and better spaced
        profileButton.addActionListener(e -> cont.viewProfile());
        cp.add(profileButton);

        // Page Title - Centered
        pageTitle.setFont(new Font("Arial", Font.BOLD, 18)); // Bigger and bold title
        pageTitle.setBounds(W / 2 - 100, 10, 200, 30);
        pageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(pageTitle);

        // Search Field and Button
        searchField.setBounds(20, 60, 400, 30); // Wider search bar
        cp.add(searchField);

        searchButton.setBounds(430, 60, 100, 30); // Positioned next to the search bar
        searchButton.addActionListener(e -> cont.doSearch(searchField.getText()));
        cp.add(searchButton);

        // Basket Button
        basketButton.setBounds(550, 60, 100, 30); // Aligned to the right
        basketButton.addActionListener(e -> cont.viewBasket()); // Redirect to BasketView
        cp.add(basketButton);

        // Product Table
        productScrollPane.setBounds(20, 110, 660, 300); // Centered and larger
        cp.add(productScrollPane);

        // Action Label
        theAction.setBounds(20, 420, 660, 20); // Placed below the table
        theAction.setHorizontalAlignment(SwingConstants.CENTER);
        cp.add(theAction);

        // Add to Basket Button
        addToBasketButton.setBounds(150, 450, 150, 40); // Center-left
        addToBasketButton.addActionListener(e -> {
            Product selectedProduct = getSelectedProduct();
            if (selectedProduct != null) {
                // Show quantity selection dialog
                String quantityStr = JOptionPane.showInputDialog(null,
                        "Enter quantity to add to basket (Available: " + selectedProduct.getQuantity() + "):",
                        "Select Quantity",
                        JOptionPane.PLAIN_MESSAGE);

                if (quantityStr != null) {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        if (quantity > 0 && quantity <= selectedProduct.getQuantity()) {
                            // Pass the product and quantity to the controller
                            cont.addToBasket(selectedProduct, quantity);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Invalid quantity. Please enter a number between 1 and " + selectedProduct.getQuantity() + ".",
                                    "Invalid Input",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid input. Please enter a valid number.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cp.add(addToBasketButton);

        rootWindow.setVisible(true);
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
                System.out.println("Updating product table..."); // Debug: Log updates
                populateProductTable(model.getProducts()); // Refresh with updated stock levels
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

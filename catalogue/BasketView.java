package catalogue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BasketView {
    private final Basket basket;
    private final DefaultTableModel tableModel;
    private final JFrame frame;

    public BasketView(Basket basket, BasketController controller) {
        this.basket = basket;

        // Create the frame
        frame = new JFrame("Your Basket");
        frame.setSize(600, 400);
        frame.setLayout(null);

        // Create the table
        JTable basketTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"Product No", "Description", "Quantity", "Total Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };
        basketTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(basketTable);
        scrollPane.setBounds(20, 20, 540, 200);
        frame.add(scrollPane);

        // Create the Remove button
        JButton removeButton = new JButton("Remove Selected");
        removeButton.setBounds(20, 240, 150, 30);
        removeButton.addActionListener(e -> {
            int selectedRow = basketTable.getSelectedRow();
            if (selectedRow != -1) {
                Product product = basket.get(selectedRow);
                basket.remove(product); // Remove from basket
                controller.updateBasketView(); // Refresh the view
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to remove.");
            }
        });
        frame.add(removeButton);

        // Create the Checkout button
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(410, 240, 150, 30);
        checkoutButton.addActionListener(e -> controller.checkoutBasket());
        frame.add(checkoutButton);
    }

    // Show the frame
    public void show() {
        frame.setVisible(true);
    }

    // Close the frame
    public void close() {
        frame.dispose();
    }

    // Update the basket table
    public void updateBasketView() {
        tableModel.setRowCount(0); // Clear the table
        for (Product product : basket) {
            tableModel.addRow(new Object[]{
                    product.getProductNum(),
                    product.getDescription(),
                    product.getQuantity(),
                    product.getPrice() * product.getQuantity()
            });
        }
    }
}

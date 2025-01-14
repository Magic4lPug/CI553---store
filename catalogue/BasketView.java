package catalogue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BasketView {
    private final Basket basket;
    private final DefaultTableModel tableModel;
    private final JFrame frame;
    private final BasketController controller;

    public BasketView(Basket basket, BasketController controller, String userID) {
        this.basket = basket;
        this.controller = controller;

        frame = new JFrame("Your Basket");
        frame.setSize(600, 400);
        frame.setLayout(null);

        JTable basketTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"Product No", "Description", "Quantity", "Total Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        basketTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(basketTable);
        scrollPane.setBounds(20, 20, 540, 200);
        frame.add(scrollPane);

        JButton removeButton = new JButton("Remove Selected");
        removeButton.setBounds(20, 240, 150, 30);
        removeButton.addActionListener(e -> {
            int selectedRow = basketTable.getSelectedRow();
            if (selectedRow != -1) {
                String productNo = (String) tableModel.getValueAt(selectedRow, 0);
                Product productToRemove = findProductInBasket(productNo);
                if (productToRemove != null) {
                    controller.removeFromBasket(productToRemove); // Remove item for current user
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select an item to remove.");
            }
        });
        frame.add(removeButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(410, 240, 150, 30);
        checkoutButton.addActionListener(e -> controller.checkoutBasket()); // Checkout for current user
        frame.add(checkoutButton);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }

    public void updateBasketView() {
        tableModel.setRowCount(0); // Clear table
        for (Product product : basket) {
            tableModel.addRow(new Object[]{
                    product.getProductNum(),
                    product.getDescription(),
                    product.getQuantity(),
                    product.getPrice() * product.getQuantity()
            });
        }
    }

    private Product findProductInBasket(String productNo) {
        for (Product product : basket) {
            if (product.getProductNum().equals(productNo)) {
                return product;
            }
        }
        return null;
    }
}

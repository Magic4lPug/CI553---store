package catalogue;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class BasketView {
    private final Basket basket;
    private final DefaultTableModel tableModel;
    private final JFrame frame;
    private final BasketController controller;

    public BasketView(Basket basket, BasketController controller, String userID) {
        this.basket = basket;
        this.controller = controller;

        // Frame setup
        frame = new JFrame("Your Basket");
        frame.setSize(600, 400);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(46, 46, 46)); // Dark background

        // Table setup
        JTable basketTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"Product No", "Description", "Quantity", "Total Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        basketTable.setModel(tableModel);
        basketTable.setBackground(new Color(255, 215, 0)); // Gold background
        basketTable.setForeground(Color.BLACK); // Black text
        basketTable.setSelectionBackground(new Color(0, 0, 0)); // Black selection
        basketTable.setSelectionForeground(new Color(255, 215, 0)); // Gold selection text

        JTableHeader header = basketTable.getTableHeader();
        header.setBackground(Color.BLACK);
        header.setForeground(new Color(255, 215, 0));
        header.setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(basketTable);
        scrollPane.setBounds(20, 20, 540, 200);
        frame.add(scrollPane);

        // Remove button setup
        JButton removeButton = new JButton("Remove Selected");
        removeButton.setBounds(20, 240, 150, 30);
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(new Color(255, 215, 0));
        removeButton.setFont(new Font("Arial", Font.BOLD, 12));
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

        // Checkout button setup
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.setBounds(410, 240, 150, 30);
        checkoutButton.setBackground(Color.BLACK);
        checkoutButton.setForeground(new Color(255, 215, 0));
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 12));
        checkoutButton.addActionListener(e -> controller.checkoutBasket()); // Checkout for current user
        frame.add(checkoutButton);

        // Title label setup
        JLabel titleLabel = new JLabel("Your Basket", SwingConstants.CENTER);
        titleLabel.setBounds(20, 280, 540, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(255, 215, 0));
        frame.add(titleLabel);
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

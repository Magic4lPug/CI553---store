package clients.cashier;

import catalogue.Basket;
import middle.MiddleFactory;

import javax.swing.table.DefaultTableModel;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CompletedOrdersModel {
    private final Connection databaseConnection;

    public CompletedOrdersModel(MiddleFactory mf) {
        try {
            databaseConnection = mf.makeDatabaseConnection();
        } catch (Exception e) {
            throw new RuntimeException("Error initializing database connection: " + e.getMessage());
        }
    }

    public DefaultTableModel getCompletedOrdersData() {
        String[] columns = {"Order ID", "Items", "Total Price"};
        List<Object[]> rows = new ArrayList<>();

        try (PreparedStatement ps = databaseConnection.prepareStatement(
                "SELECT taskID, taskData FROM TaskTable WHERE status = 'Completed'")) { // Corrected to 'Completed'
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int taskID = rs.getInt("taskID");
                Basket basket = deserializeBasket(rs.getBytes("taskData"));
                double total = basket.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
                rows.add(new Object[]{taskID, basket.size(), total});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DefaultTableModel(rows.toArray(new Object[0][0]), columns);
    }

    public DefaultTableModel searchCompletedOrder(int orderID) {
        String[] columns = {"Order ID", "Items", "Total Price"};
        List<Object[]> rows = new ArrayList<>();

        try (PreparedStatement ps = databaseConnection.prepareStatement(
                "SELECT taskID, taskData FROM TaskTable WHERE taskID = ? AND status = 'Completed'")) { // Corrected to 'Completed'
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Basket basket = deserializeBasket(rs.getBytes("taskData"));
                double total = basket.stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();
                rows.add(new Object[]{orderID, basket.size(), total});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DefaultTableModel(rows.toArray(new Object[0][0]), columns);
    }

    private Basket deserializeBasket(byte[] data) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Basket) ois.readObject();
        }
    }
}

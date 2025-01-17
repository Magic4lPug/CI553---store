package clients.backDoor;

import clients.customer.CustomerModel;
import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone BackDoor Client.
 */
public class BackDoorClient {
    public static void main(String[] args) {
        String stockURL = args.length < 1 ? Names.STOCK_RW : args[0];
        String orderURL = args.length < 2 ? Names.ORDER : args[1];

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL); // Ensure this sets the correct StockReadWriter URL
        mrf.setOrderInfo(orderURL);   // Set order processing URL

        // CustomerModel must be initialized and passed here
        CustomerModel customerModel = new CustomerModel(mrf); // Initialise CustomerModel
        displayGUI(mrf, customerModel); // Pass factory and CustomerModel to GUI
    }

    /**
     * Display the BackDoor GUI.
     * @param mf MiddleFactory instance for creating necessary components.
     * @param customerModel CustomerModel to update products after stock changes.
     */
    private static void displayGUI(MiddleFactory mf, CustomerModel customerModel) {
        JFrame window = new JFrame();

        window.setTitle("BackDoor Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            BackDoorModel model = new BackDoorModel(mf.makeStockReadWriter()); // Create BackDoorModel using factory
            BackDoorView view = new BackDoorView(mf);                         // Pass factory to view
            BackDoorController controller = new BackDoorController(model, view);
            controller.setCustomerModel(customerModel); // Link CustomerModel

            view.setController(controller);
            model.addObserver(view);

            window.setContentPane(view.getPanel());
            window.pack();
            window.setSize(400, 300);
            window.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error initializing BackDoorClient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

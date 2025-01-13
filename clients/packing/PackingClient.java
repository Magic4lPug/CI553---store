package clients.packing;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone warehouse Packing Client. Warehouse staff to pack the bought order.
 * @author  Mike
 * @version 2.0
 */
public class PackingClient {
    public static void main(String[] args) {
        String stockURL = args.length < 1     // URL of stock RW
                ? Names.STOCK_RW             // default location
                : args[0];                   // supplied location
        String orderURL = args.length < 2    // URL of order
                ? Names.ORDER                // default location
                : args[1];                   // supplied location

        RemoteMiddleFactory mrf = new RemoteMiddleFactory();
        mrf.setStockRWInfo(stockURL);
        mrf.setOrderInfo(orderURL);
        displayGUI(mrf); // Create GUI
    }

    public static void displayGUI(MiddleFactory mf) {
        JFrame window = new JFrame();

        window.setTitle("Packing Client (RMI MVC)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PackingModel model = new PackingModel(mf);
        PackingView view = new PackingView(mf); // Updated to match the new constructor
        PackingController cont = new PackingController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model

        window.setContentPane(view.getPanel()); // Embed the JPanel in the JFrame
        window.pack();                          // Adjust the frame to fit content
        window.setSize(400, 300);               // Optional: Set window size explicitly
        window.setVisible(true);                // Display Screen
    }
}

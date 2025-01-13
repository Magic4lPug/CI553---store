package clients.backDoor;

import middle.MiddleFactory;
import middle.Names;
import middle.RemoteMiddleFactory;

import javax.swing.*;

/**
 * The standalone BackDoor Client
 */
public class BackDoorClient {
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

    private static void displayGUI(MiddleFactory mf) {
        JFrame window = new JFrame();

        window.setTitle("BackDoor Client (MVC RMI)");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackDoorModel model = new BackDoorModel(mf);
        BackDoorView view = new BackDoorView(mf); // Updated to match the new constructor
        BackDoorController cont = new BackDoorController(model, view);
        view.setController(cont);

        model.addObserver(view); // Add observer to the model

        window.setContentPane(view.getPanel()); // Embed the JPanel in the JFrame
        window.pack();                          // Adjust the frame to fit content
        window.setSize(400, 300);               // Optional: Set window size explicitly
        window.setVisible(true);                // Display Screen
    }
}

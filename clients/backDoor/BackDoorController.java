package clients.backDoor;

import clients.customer.CustomerModel;

/**
 * Handles user actions for the BackDoor.
 */
public class BackDoorController {
  private final BackDoorModel model;
  private final BackDoorView view;

  public BackDoorController(BackDoorModel model, BackDoorView view) {
    this.model = model;
    this.view = view;
  }

  public void setCustomerModel(CustomerModel customerModel) {
    model.setCustomerModel(customerModel);
  }

  public void doQuery(String productNum) {
    model.doQuery(productNum);
  }

  public void doRestock(String productNum, String quantity) {
    model.doRestock(productNum, quantity);
  }

  public void doRemoveStock(String productNum, String quantity) {
    model.doRemoveStock(productNum, quantity);
  }
}

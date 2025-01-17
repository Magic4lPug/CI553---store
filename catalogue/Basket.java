package catalogue;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A collection of products,
 * used to record the products that are to be wished to be purchased.

 */
public class Basket extends ArrayList<Product> implements Serializable {
  private static final long serialVersionUID = 1;
  private int theOrderNum = 0; // Order number

  // Default no-argument constructor
  public Basket() {
    super(); // Call ArrayList's constructor
    this.theOrderNum = 0; // Initialize the order number to 0
  }

  // Copy constructor
  public Basket(Basket other) {
    super(other); // Copy all products
    this.theOrderNum = other.theOrderNum; // Copy the order number
  }

  public void setOrderNum(int anOrderNum) {
    theOrderNum = anOrderNum;
  }

  public int getOrderNum() {
    return theOrderNum;
  }

  @Override
  public boolean add(Product pr) {
    return super.add(pr);
  }

  public String getDetails() {
    StringBuilder sb = new StringBuilder();
    double total = 0.00;

    if (theOrderNum != 0) {
      sb.append("Order number: ").append(theOrderNum).append("\n");
    }

    if (!this.isEmpty()) {
      for (Product pr : this) {
        sb.append(String.format("%-7s %-14.14s (%3d) %.2f\n",
                pr.getProductNum(),
                pr.getDescription(),
                pr.getQuantity(),
                pr.getPrice() * pr.getQuantity()));
        total += pr.getPrice() * pr.getQuantity();
      }
      sb.append("----------------------------\n");
      sb.append(String.format("Total                       %.2f\n", total));
    }
    return sb.toString();
  }

  /**
   * Returns the list of products in the basket.
   * @return List of products.
   */
  public ArrayList<Product> getItems() {
    return new ArrayList<>(this);
  }

  private boolean packed = false;

  public boolean isPacked() {
    return packed;
  }

  public void setPacked(boolean packed) {
    this.packed = packed;
  }

}

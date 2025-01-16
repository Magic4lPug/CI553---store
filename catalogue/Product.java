package catalogue;

import java.io.Serializable;

/**
 * Used to hold the following information about
 * a product: Product number, Description, Price, Stock level.
 * @author  Mike Smith University of Brighton
 * @version 2.0
 */

public class Product implements Serializable {
  private static final long serialVersionUID = 20092506;
  private String theProductNum;   // Product number
  private String theDescription;  // Description of product
  private double thePrice;        // Price of product
  private int theQuantity;        // Quantity involved
  private String thePicture;      // Path to the product picture

  /**
   * Construct a product details
   * @param aProductNum Product number
   * @param aDescription Description of product
   * @param aPrice The price of the product
   * @param aQuantity The Quantity of the product involved
   * @param aPicture Path to the picture of the product
   */
  public Product(String aProductNum, String aDescription, double aPrice, int aQuantity, String aPicture) {
    theProductNum = aProductNum;
    theDescription = aDescription;
    thePrice = aPrice;
    theQuantity = aQuantity;
    thePicture = aPicture;
  }

  public String getProductNum() { return theProductNum; }
  public String getDescription() { return theDescription; }
  public double getPrice() { return thePrice; }
  public int getQuantity() { return theQuantity; }
  public String getPicture() { return thePicture; }

  public void setProductNum(String aProductNum) { theProductNum = aProductNum; }
  public void setDescription(String aDescription) { theDescription = aDescription; }
  public void setPrice(double aPrice) { thePrice = aPrice; }
  public void setQuantity(int aQuantity) { theQuantity = aQuantity; }
  public void setPicture(String aPicture) { thePicture = aPicture; }
}

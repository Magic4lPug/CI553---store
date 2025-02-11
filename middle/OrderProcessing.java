package middle;

import catalogue.Basket;

import java.util.List;
import java.util.Map;

/**
  * Defines the interface for accessing the order processing system.
  */

public interface OrderProcessing
{
                                                   // Used by
  public void newOrder(Basket bought)              // Cashier
         throws OrderException;

  public int  uniqueNumber()                       // Cashier
         throws OrderException;
   
  public Basket getOrderToPack()                   // Packer
         throws OrderException;
 
  public boolean informOrderPacked(int orderNum)   // Packer 
         throws OrderException;
         
  // not being used in this version
  public boolean informOrderCollected(int orderNum) // Collection
         throws OrderException;
   
  // not being used in this version
  public Map<String,List<Integer>> getOrderState() // Display
         throws OrderException;
}

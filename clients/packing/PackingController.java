package clients.packing;

import clients.packing.PackingModel;
import clients.packing.PackingView;
/**
 * The Packing Controller
 */

public class PackingController 
{
  private PackingModel model = null;
  private PackingView  view  = null;
  /**
   * Constructor
   */
  public PackingController( PackingModel model, PackingView view )
  {
    this.view  = view;
    this.model = model;
  }

  /**
   * Picked interaction from view
   */
  public void doPacked()
  {
    model.doPacked();
  }
  
}


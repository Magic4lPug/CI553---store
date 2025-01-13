package dbAccess;

/**
  * Apache Derby database access
  * @author  Mike Smith University of Brighton
  * @version 2.0
  */
 
class DerbyAccess extends DBAccess
{
  private static final String URLdb =
          "jdbc:derby:catshop.db;create=true";
  private static final String DRIVER =
                 "org.apache.derby.jdbc.EmbeddedDriver";

  /**
   * Load the Apache Derby database driver
   */
  public void loadDriver() throws Exception {
    try {
      System.out.println("Loading driver: " + DRIVER);
      Class.forName(DRIVER).newInstance();
      System.out.println("Driver loaded successfully.");
    } catch (Exception e) {
      System.err.println("Failed to load driver: " + e.getMessage());
      throw e;
    }
  }

  /**
   * Return the url to access the database
   * @return url to database
   */
  public String urlOfDatabase()
  {
    return URLdb;
  }
}


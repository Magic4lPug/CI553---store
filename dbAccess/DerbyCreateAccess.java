package dbAccess;

/**
  * Implements management of an Apache Derby database.
  *  that is too be created

  */
 
class DerbyCreateAccess extends DBAccess
{
  private static final String URLdb = "jdbc:sqlite:catshop.db";
  private static final String DRIVER = "org.sqlite.JDBC";


  public void loadDriver() throws Exception
  {
    Class.forName(DRIVER).newInstance();
  }

  public String urlOfDatabase()
  {
    return URLdb;
  }
}


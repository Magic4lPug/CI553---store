package dbAccess;

/**
  * Implements management of an mySQL database on Linux.

  */
class LinuxAccess extends DBAccess
{
  public void loadDriver() throws Exception
  {
    Class.forName("org.gjt.mm.mysql.Driver").newInstance();
  }

  public String urlOfDatabase()
  {
    return "jdbc:mysql://localhost/cshop?user=root";
  }
}

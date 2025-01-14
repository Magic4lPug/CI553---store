package dbAccess;

public class DerbyAccess extends DBAccess {
  private static final String URLdb = "jdbc:derby:catshop.db;create=true";
  private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

  @Override
  public void loadDriver() throws Exception {
    try {
      Class.forName(DRIVER).newInstance();
      System.out.println("Derby driver loaded successfully.");
    } catch (Exception e) {
      throw new Exception("Failed to load Derby driver: " + e.getMessage());
    }
  }

  @Override
  public String urlOfDatabase() {
    return URLdb;
  }

  @Override
  public String username() {
    return ""; // Adjust if Derby requires authentication
  }

  @Override
  public String password() {
    return ""; // Adjust if Derby requires authentication
  }
}

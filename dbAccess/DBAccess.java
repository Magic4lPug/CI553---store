package dbAccess;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBAccess {
  private String url = "";
  private String username = "";
  private String password = "";

  public void loadDriver() throws Exception {
    throw new RuntimeException("No driver");
  }

  public String urlOfDatabase() {
    return url;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  public Connection getConnection() throws Exception {
    loadDriver();
    return DriverManager.getConnection(urlOfDatabase(), username(), password());
  }
}

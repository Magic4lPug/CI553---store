package dbAccess;

public class SQLiteAccess extends DBAccess {
    private static final String URLdb = "jdbc:sqlite:catshop.db";

    @Override
    public void loadDriver() throws Exception {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            System.out.println("SQLite driver loaded successfully.");
        } catch (Exception e) {
            throw new Exception("Failed to load SQLite driver: " + e.getMessage());
        }
    }

    @Override
    public String urlOfDatabase() {
        return URLdb;
    }

    @Override
    public String username() {
        return ""; // SQLite doesn't require a username
    }

    @Override
    public String password() {
        return ""; // SQLite doesn't require a password
    }
}

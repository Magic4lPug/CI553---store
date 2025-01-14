package dbAccess;

import debug.DEBUG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Manages the starting up of the database.
 * The database may be SQLite, MySQL, etc.
 *
 * Implements an Abstract Factory pattern.
 */
public class DBAccessFactory {
  private static String theAction = "";
  private static String theDataBase = "";
  private static String theOS = "";

  /**
   * Sets the action (e.g., Create).
   * @param name Action name to set.
   */
  public static void setAction(String name) {
    theAction = name;
  }

  /**
   * Sets up the environment, determining database type and OS details.
   * @return The current operating system details.
   */
  private static String setEnvironment() {
    theDataBase = fileToString("DataBase.txt") + theAction;
    String os = System.getProperties().getProperty("os.name");
    String arch = System.getProperties().getProperty("os.arch");
    String osVer = System.getProperties().getProperty("os.version");
    theOS = String.format("%s %s %s", os, osVer, arch);
    System.out.println(theOS);
    return theOS;
  }

  /**
   * Returns an object to implement system-level access to the database.
   * @return An object that provides system-level access to the database.
   */
  public DBAccess getNewDBAccess() {
    setEnvironment();
    DEBUG.traceA("Using [%s] as database type\n", theDataBase);

    switch (theDataBase) {
      case "Derby":
        return new DerbyAccess();

      case "DerbyCreate":
        return new DerbyCreateAccess();

      case "SQLite":
      case "SQLiteCreate":
        return new SQLiteAccess(); // Unified SQLite implementation

      default:
        DEBUG.error("Database [%s] not known\n", theDataBase);
        System.exit(0);
    }

    return new DBAccess(); // Fallback for unknown database type
  }

  /**
   * Reads the contents of a file as a string, stripping out newline and carriage returns.
   * @param file File name to read.
   * @return The contents of the file as a string.
   */
  private static String fileToString(String file) {
    byte[] vec = fileToBytes(file);
    return new String(vec).replaceAll("\n", "").replaceAll("\r", "");
  }

  /**
   * Reads the contents of a file as a byte array.
   * @param file File name to read.
   * @return The contents of the file as a byte array.
   */
  private static byte[] fileToBytes(String file) {
    byte[] vec = new byte[0];
    try {
      final int len = (int) length(file);
      if (len < 1000) {
        vec = new byte[len];
        FileInputStream istream = new FileInputStream(file);
        final int read = istream.read(vec, 0, len);
        istream.close();
        return vec;
      } else {
        DEBUG.error("File %s length %d bytes too long", file, len);
      }
    } catch (FileNotFoundException err) {
      DEBUG.error("File does not exist: fileToBytes [%s]\n", file);
      System.exit(0);
    } catch (IOException err) {
      DEBUG.error("IO error: fileToBytes [%s]\n", file);
      System.exit(0);
    }
    return vec;
  }

  /**
   * Returns the number of characters in a file.
   * @param path File path to check.
   * @return Number of characters in the file.
   */
  private static long length(String path) {
    try {
      File in = new File(path);
      return in.length();
    } catch (SecurityException err) {
      DEBUG.error("Security error: length of file [%s]\n", path);
      System.exit(0);
    }
    return -1;
  }
}

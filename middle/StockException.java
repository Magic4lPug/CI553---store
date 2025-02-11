package middle;

/**
 * Custom exception for stock-related operations.
 */
public class StockException extends Exception {
  public StockException(String message) {
    super(message);
  }

  public StockException(String message, Throwable cause) {
    super(message, cause);
  }
}

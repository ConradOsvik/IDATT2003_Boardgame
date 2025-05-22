package edu.ntnu.stud.boardgame.exception.files;

/**
 * Exception thrown when there is an error processing board configuration files.
 *
 * <p>This exception is specifically used to indicate problems related to board files,
 * such as invalid format, missing required data, or inability to read/write board configuration
 * files.
 *
 * @see FileException
 */
public class BoardFileException extends FileException {

  /**
   * Constructs a new board file exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   */
  public BoardFileException(String message) {
    super(message);
  }

  /**
   * Constructs a new board file exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
   *                method). A {@code null} value is permitted, and indicates that the cause is
   *                nonexistent or unknown.
   */
  public BoardFileException(String message, Throwable cause) {
    super(message, cause);
  }
}

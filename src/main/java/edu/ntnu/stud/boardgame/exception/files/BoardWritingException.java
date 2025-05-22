package edu.ntnu.stud.boardgame.exception.files;

/**
 * Exception thrown when there is an error writing board configuration files.
 *
 * <p>This exception is specifically used to indicate problems encountered while writing board data
 * to files, such as permission issues, I/O errors, or failures in serializing board configuration
 * to the required output format.
 *
 * @see BoardFileException
 * @see FileException
 */
public class BoardWritingException extends BoardFileException {

  /**
   * Constructs a new board writing exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   */
  public BoardWritingException(String message) {
    super(message);
  }

  /**
   * Constructs a new board writing exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public BoardWritingException(String message, Throwable cause) {
    super(message, cause);
  }
}

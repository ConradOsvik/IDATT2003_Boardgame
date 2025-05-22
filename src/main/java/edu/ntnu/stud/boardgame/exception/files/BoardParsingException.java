package edu.ntnu.stud.boardgame.exception.files;

/**
 * Exception thrown when there is an error parsing board configuration files.
 *
 * <p>This exception is specifically used to indicate problems encountered while parsing the content
 * of board files, such as invalid syntax, unrecognized format, or data that does not conform to the
 * expected structure.
 *
 * @see BoardFileException
 * @see FileException
 */
public class BoardParsingException extends BoardFileException {

  /**
   * Constructs a new board parsing exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   */
  public BoardParsingException(String message) {
    super(message);
  }

  /**
   * Constructs a new board parsing exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public BoardParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}

package edu.ntnu.stud.boardgame.exception.files;

/**
 * Exception thrown when there is an error parsing player data from files.
 *
 * <p>This exception is specifically used to indicate problems encountered while parsing or
 * interpreting player data from configuration files. These issues may include invalid format,
 * corrupted data, missing required fields, or incompatible data types when attempting to read
 * player information.
 *
 * @see PlayerFileException
 * @see FileException
 */
public class PlayerParsingException extends PlayerFileException {

  /**
   * Constructs a new player parsing exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   */
  public PlayerParsingException(String message) {
    super(message);
  }

  /**
   * Constructs a new player parsing exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public PlayerParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}

package edu.ntnu.stud.boardgame.exception.files;

/**
 * Exception thrown when there is an error related to player file operations.
 *
 * <p>This exception is specifically used to indicate problems encountered while reading, writing,
 * or processing player data files, such as player profiles, saved game states, or player
 * configuration files.</p>
 *
 * @see FileException
 * @see BoardFileException
 */
public class PlayerFileException extends FileException {

  /**
   * Constructs a new player file exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   */
  public PlayerFileException(String message) {
    super(message);
  }

  /**
   * Constructs a new player file exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
   *                method). A {@code null} value is permitted, and indicates that the cause is
   *                nonexistent or unknown.
   */
  public PlayerFileException(String message, Throwable cause) {
    super(message, cause);
  }
}

package edu.ntnu.stud.boardgame.exception.files;

/**
 * Exception thrown when there is an error writing player data to files.
 *
 * <p>This exception is specifically used to indicate problems encountered while writing or saving
 * player data to files. These issues may include file permission errors, disk space limitations,
 * I/O failures, or serialization problems when attempting to persist player information.
 *
 * @see PlayerFileException
 * @see FileException
 * @see PlayerParsingException
 */
public class PlayerWritingException extends PlayerFileException {

  /**
   * Constructs a new player writing exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   */
  public PlayerWritingException(String message) {
    super(message);
  }

  /**
   * Constructs a new player writing exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public PlayerWritingException(String message, Throwable cause) {
    super(message, cause);
  }
}

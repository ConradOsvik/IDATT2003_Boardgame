package edu.ntnu.stud.boardgame.exception.files;

import edu.ntnu.stud.boardgame.exception.BoardGameException;

/**
 * Exception thrown when there is an error related to file operations.
 *
 * <p>This exception serves as the base class for all file-related exceptions in the board game
 * application. It represents general file operation errors such as file access problems, I/O
 * errors, or other issues related to reading or writing files.</p>
 *
 * @see BoardGameException
 * @see BoardFileException
 * @see BoardParsingException
 * @see BoardWritingException
 */
public class FileException extends BoardGameException {

  /**
   * Constructs a new file exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   */
  public FileException(String message) {
    super(message);
  }

  /**
   * Constructs a new file exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *                {@link #getMessage()} method)
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()}
   *                method). A {@code null} value is permitted, and indicates that the cause is
   *                nonexistent or unknown.
   */
  public FileException(String message, Throwable cause) {
    super(message, cause);
  }
}

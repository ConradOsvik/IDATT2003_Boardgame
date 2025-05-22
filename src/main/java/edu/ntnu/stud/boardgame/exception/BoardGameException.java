package edu.ntnu.stud.boardgame.exception;

/**
 * Base exception for all board game application-specific exceptions.
 *
 * <p>This exception serves as the root of the exception hierarchy for the board game application.
 * It provides a common superclass for all domain-specific exceptions that may occur during the
 * execution of the application, allowing for consistent error handling and categorization of
 * different types of failures.
 *
 * @see edu.ntnu.stud.boardgame.exception.files.FileException
 */
public class BoardGameException extends Exception {

  /**
   * Constructs a new board game exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   */
  public BoardGameException(String message) {
    super(message);
  }

  /**
   * Constructs a new board game exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public BoardGameException(String message, Throwable cause) {
    super(message, cause);
  }
}

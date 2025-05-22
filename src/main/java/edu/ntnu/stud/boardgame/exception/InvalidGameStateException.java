package edu.ntnu.stud.boardgame.exception;

/**
 * Exception thrown when the game is in an invalid state for an operation.
 *
 * <p>This exception is used to indicate that an operation cannot be performed because the game is
 * in an inappropriate state. For example, attempting to make a move when the game hasn't started,
 * trying to add players when the game is already in progress, or accessing game elements that
 * aren't available in the current state.
 *
 * <p>Unlike most other exceptions in the board game application, this class extends {@link
 * IllegalStateException} from the Java standard library rather than {@link BoardGameException},
 * making it a runtime exception that doesn't require explicit handling.
 *
 * @see BoardGameException
 * @see IllegalStateException
 */
public class InvalidGameStateException extends IllegalStateException {

  /**
   * Constructs a new invalid game state exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   */
  public InvalidGameStateException(String message) {
    super(message);
  }

  /**
   * Constructs a new invalid game state exception with the specified detail message and cause.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method)
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.
   */
  public InvalidGameStateException(String message, Throwable cause) {
    super(message, cause);
  }
}

package edu.ntnu.stud.boardgame.exception;

public class InvalidGameStateException extends IllegalStateException {

  public InvalidGameStateException(String message) {
    super(message);
  }

  public InvalidGameStateException(String message, Throwable cause) {
    super(message, cause);
  }
}

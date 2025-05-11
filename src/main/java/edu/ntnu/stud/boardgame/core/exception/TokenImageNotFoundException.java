package edu.ntnu.stud.boardgame.core.exception;

public class TokenImageNotFoundException extends RuntimeException {

  public TokenImageNotFoundException(String message) {
    super(message);
  }

  public TokenImageNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}

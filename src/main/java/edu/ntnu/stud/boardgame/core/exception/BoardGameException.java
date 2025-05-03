package edu.ntnu.stud.boardgame.core.exception;

public class BoardGameException extends RuntimeException {

  public BoardGameException(String message) {
    super(message);
  }

  public BoardGameException(String message, Throwable cause) {
    super(message, cause);
  }
}
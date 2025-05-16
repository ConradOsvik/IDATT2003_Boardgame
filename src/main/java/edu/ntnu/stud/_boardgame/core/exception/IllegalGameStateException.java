package edu.ntnu.stud.boardgame.core.exception;

public class IllegalGameStateException extends BoardGameException {

  public IllegalGameStateException(String message) {
    super(message);
  }
}
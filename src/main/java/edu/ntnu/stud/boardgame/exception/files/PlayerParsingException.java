package edu.ntnu.stud.boardgame.exception.files;

public class PlayerParsingException extends PlayerFileException {

  public PlayerParsingException(String message) {
    super(message);
  }

  public PlayerParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}

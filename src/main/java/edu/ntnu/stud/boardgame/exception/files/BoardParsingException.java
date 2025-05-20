package edu.ntnu.stud.boardgame.exception.files;

public class BoardParsingException extends BoardFileException {

  public BoardParsingException(String message) {
    super(message);
  }

  public BoardParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}

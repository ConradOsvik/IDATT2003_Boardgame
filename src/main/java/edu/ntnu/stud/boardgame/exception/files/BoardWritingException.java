package edu.ntnu.stud.boardgame.exception.files;

public class BoardWritingException extends BoardFileException {

  public BoardWritingException(String message) {
    super(message);
  }

  public BoardWritingException(String message, Throwable cause) {
    super(message, cause);
  }
}

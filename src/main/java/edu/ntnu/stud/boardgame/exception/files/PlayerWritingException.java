package edu.ntnu.stud.boardgame.exception.files;

public class PlayerWritingException extends PlayerFileException {

  public PlayerWritingException(String message) {
    super(message);
  }

  public PlayerWritingException(String message, Throwable cause) {
    super(message, cause);
  }
}

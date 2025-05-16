package edu.ntnu.stud.boardgame.exception.files;

import edu.ntnu.stud.boardgame.exception.BoardGameException;

public class FileException extends BoardGameException {

  public FileException(String message) {
    super(message);
  }

  public FileException(String message, Throwable cause) {
    super(message, cause);
  }
}

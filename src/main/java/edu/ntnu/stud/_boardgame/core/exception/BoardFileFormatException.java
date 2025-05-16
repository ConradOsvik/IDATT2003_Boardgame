package edu.ntnu.stud.boardgame.core.exception;

import java.io.IOException;

public class BoardFileFormatException extends IOException {

  public BoardFileFormatException(String message) {
    super(message);
  }

  public BoardFileFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}
package edu.ntnu.stud.boardgame.core.exception;

import java.io.IOException;

public class PlayerFileFormatException extends IOException {

  public PlayerFileFormatException(String message) {
    super(message);
  }

  public PlayerFileFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}
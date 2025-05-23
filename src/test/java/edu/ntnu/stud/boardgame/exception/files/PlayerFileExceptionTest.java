package edu.ntnu.stud.boardgame.exception.files;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerFileExceptionTest {

  @Test
  void constructor_withMessage_initializesCorrectly() {
    String message = "Player File Exception";
    PlayerFileException exception = new PlayerFileException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void constructor_withMessageAndCause_initializesCorrectly() {
    String message = "Player File Exception with Cause";
    Throwable cause = new RuntimeException("Root cause of player file error");
    PlayerFileException exception = new PlayerFileException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}

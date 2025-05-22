package edu.ntnu.stud.boardgame.exception.files;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerWritingExceptionTest {

  @Test
  void constructor_withMessage_initializesCorrectly() {
    String message = "Player Writing Exception";
    PlayerWritingException exception = new PlayerWritingException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void constructor_withMessageAndCause_initializesCorrectly() {
    String message = "Player Writing Exception with Cause";
    Throwable cause = new RuntimeException("Root cause of player writing error");
    PlayerWritingException exception = new PlayerWritingException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}

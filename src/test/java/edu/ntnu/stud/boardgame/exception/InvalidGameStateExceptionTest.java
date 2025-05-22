package edu.ntnu.stud.boardgame.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvalidGameStateExceptionTest {

  @Test
  void constructor_withMessage_initializesCorrectly() {
    String message = "Invalid Game State";
    InvalidGameStateException exception = new InvalidGameStateException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void constructor_withMessageAndCause_initializesCorrectly() {
    String message = "Invalid Game State with Cause";
    Throwable cause = new RuntimeException("Root cause of invalid state");
    InvalidGameStateException exception = new InvalidGameStateException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}

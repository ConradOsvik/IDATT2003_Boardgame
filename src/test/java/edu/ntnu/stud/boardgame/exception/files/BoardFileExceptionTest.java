package edu.ntnu.stud.boardgame.exception.files;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardFileExceptionTest {

  @Test
  void constructor_withMessage_initializesCorrectly() {
    String message = "Board File Exception";
    BoardFileException exception = new BoardFileException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void constructor_withMessageAndCause_initializesCorrectly() {
    String message = "Board File Exception with Cause";
    Throwable cause = new RuntimeException("Root cause of board file error");
    BoardFileException exception = new BoardFileException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}

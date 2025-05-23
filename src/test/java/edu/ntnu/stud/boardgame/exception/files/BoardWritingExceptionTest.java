package edu.ntnu.stud.boardgame.exception.files;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardWritingExceptionTest {

  @Test
  void constructor_withMessage_initializesCorrectly() {
    String message = "Board Writing Exception";
    BoardWritingException exception = new BoardWritingException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void constructor_withMessageAndCause_initializesCorrectly() {
    String message = "Board Writing Exception with Cause";
    Throwable cause = new RuntimeException("Root cause of board writing error");
    BoardWritingException exception = new BoardWritingException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }
}

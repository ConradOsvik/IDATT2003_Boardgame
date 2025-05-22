package edu.ntnu.stud.boardgame.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardGameExceptionTest {

    @Test
    void constructor_withMessage_initializesCorrectly() {
        String message = "Test Exception Message";
        BoardGameException exception = new BoardGameException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_withMessageAndCause_initializesCorrectly() {
        String message = "Test Exception Message with Cause";
        Throwable cause = new RuntimeException("Root cause");
        BoardGameException exception = new BoardGameException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
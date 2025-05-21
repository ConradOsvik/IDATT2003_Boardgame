package edu.ntnu.stud.boardgame.exception.files;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardParsingExceptionTest {

    @Test
    void constructor_withMessage_initializesCorrectly() {
        String message = "Board Parsing Exception";
        BoardParsingException exception = new BoardParsingException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_withMessageAndCause_initializesCorrectly() {
        String message = "Board Parsing Exception with Cause";
        Throwable cause = new RuntimeException("Root cause of board parsing error");
        BoardParsingException exception = new BoardParsingException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
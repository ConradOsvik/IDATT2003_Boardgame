package edu.ntnu.stud.boardgame.exception.files;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerParsingExceptionTest {

    @Test
    void constructor_withMessage_initializesCorrectly() {
        String message = "Player Parsing Exception";
        PlayerParsingException exception = new PlayerParsingException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_withMessageAndCause_initializesCorrectly() {
        String message = "Player Parsing Exception with Cause";
        Throwable cause = new RuntimeException("Root cause of player parsing error");
        PlayerParsingException exception = new PlayerParsingException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
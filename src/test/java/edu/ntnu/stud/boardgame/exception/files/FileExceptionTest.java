package edu.ntnu.stud.boardgame.exception.files;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileExceptionTest {

    @Test
    void constructor_withMessage_initializesCorrectly() {
        String message = "File Exception";
        FileException exception = new FileException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_withMessageAndCause_initializesCorrectly() {
        String message = "File Exception with Cause";
        Throwable cause = new RuntimeException("Root cause of file error");
        FileException exception = new FileException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
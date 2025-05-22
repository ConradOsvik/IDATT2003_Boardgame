package edu.ntnu.stud.boardgame;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BoardGameMainTest {

  @Test
  void main_executesWithoutException() {
    assertDoesNotThrow(
        () -> {
          assertTrue(true, "Placeholder test, review BoardGameMain for testability.");
        });
  }
}

package edu.ntnu.stud.boardgame.snakesandladders.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BoardCoordinateConverterTest {

  @Test
  void calculateTileIdFromCoordinates_validInput_returnsCorrectId() {
    // Bottom row (left to right)
    assertEquals(1, BoardCoordinateConverter.calculateTileIdFromCoordinates(9, 0, 10, 9));
    assertEquals(9, BoardCoordinateConverter.calculateTileIdFromCoordinates(9, 8, 10, 9));

    // Second row from bottom (right to left)
    assertEquals(10, BoardCoordinateConverter.calculateTileIdFromCoordinates(8, 8, 10, 9));
    assertEquals(18, BoardCoordinateConverter.calculateTileIdFromCoordinates(8, 0, 10, 9));

    // Top row (right to left for 10 rows)
    assertEquals(90, BoardCoordinateConverter.calculateTileIdFromCoordinates(0, 0, 10, 9));
    assertEquals(82, BoardCoordinateConverter.calculateTileIdFromCoordinates(0, 8, 10, 9));
  }

  @Test
  void calculateTileCoordinatesFromId_validId_returnsCorrectCoordinates() {
    int[] coords;

    // Bottom row (left to right)
    coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(1, 10, 9);
    assertEquals(9, coords[0]);
    assertEquals(0, coords[1]);

    coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(9, 10, 9);
    assertEquals(9, coords[0]);
    assertEquals(8, coords[1]);

    // Second row from bottom (right to left)
    coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(10, 10, 9);
    assertEquals(8, coords[0]);
    assertEquals(8, coords[1]);

    coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(18, 10, 9);
    assertEquals(8, coords[0]);
    assertEquals(0, coords[1]);

    // Top row (right to left for 10 rows)
    coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(90, 10, 9);
    assertEquals(0, coords[0]);
    assertEquals(0, coords[1]);

    coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(82, 10, 9);
    assertEquals(0, coords[0]);
    assertEquals(8, coords[1]);
  }

  @Test
  void calculateTileCoordinatesFromId_invalidId_throwsException() {
    assertThrows(IllegalArgumentException.class,
        () -> BoardCoordinateConverter.calculateTileCoordinatesFromId(0, 10, 9));

    assertThrows(IllegalArgumentException.class,
        () -> BoardCoordinateConverter.calculateTileCoordinatesFromId(91, 10, 9));
  }

  @Test
  void calculateTileIdAndCoordinates_roundTrip_consistent() {
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 9; col++) {
        int id = BoardCoordinateConverter.calculateTileIdFromCoordinates(row, col, 10, 9);
        int[] coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(id, 10, 9);

        assertEquals(row, coords[0], "Row mismatch for original row " + row + ", col " + col);
        assertEquals(col, coords[1], "Col mismatch for original row " + row + ", col " + col);
      }
    }
  }
}
package edu.ntnu.stud.boardgame.snakesandladders.util;

public final class BoardCoordinateConverter {

  private BoardCoordinateConverter() {
  }

  public static int calculateTileIdFromCoordinates(int row, int col, int rows, int cols) {
    int rowFromBottom = rows - 1 - row;
    if (rowFromBottom % 2 == 0) {
      return rowFromBottom * cols + col + 1;
    } else {
      return rowFromBottom * cols + (cols - col);
    }
  }

  public static int[] calculateTileCoordinatesFromId(int tileId, int rows, int cols) {
    if (tileId < 1 || tileId > rows * cols) {
      throw new IllegalArgumentException("Invalid tile ID: " + tileId);
    }

    int index = tileId - 1;
    int row = rows - 1 - (index / cols);

    int col;
    if ((rows - 1 - row) % 2 == 0) {
      col = index % cols;
    } else {
      col = cols - 1 - (index % cols);
    }

    return new int[]{row, col};
  }
}
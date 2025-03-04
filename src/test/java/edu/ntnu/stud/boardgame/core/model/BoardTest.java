package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Board}
 */
class BoardTest {

  private Board board;
  private Tile tile;

  @BeforeEach
  void setup() {
    board = new Board();
    tile = new Tile(1);
  }

  @Test
  void addTile_validTile_addsTileToBoard() {
    board.addTile(tile);
    assertEquals(tile, board.getTile(1));
  }

  @Test
  void addTile_nullTile_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> board.addTile(null));
  }

  @Test
  void getTile_existingTileId_returnsTile() {
    board.addTile(tile);
    assertEquals(tile, board.getTile(1));
  }

  @Test
  void getTile_nonExistingTileId_returnsNull() {
    assertNull(board.getTile(999));
  }

  @Test
  void addTile_duplicateTileId_overwritesExistingTile() {
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(1);

    board.addTile(tile1);
    board.addTile(tile2);

    assertSame(tile2, board.getTile(1));
  }
}
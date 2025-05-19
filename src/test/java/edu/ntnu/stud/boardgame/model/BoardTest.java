package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("Board Tests")
class BoardTest {

  private Board board;

  @Mock
  private Tile mockTile1;

  @Mock
  private Tile mockTile2;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    board = new Board("Test Board", "A test board", 10, 9, 0, 90);

    when(mockTile1.getTileId()).thenReturn(1);
    when(mockTile2.getTileId()).thenReturn(2);
  }

  @Nested
  @DisplayName("Tile Management Tests")
  class TileManagementTests {

    @Test
    @DisplayName("addTile should add tile to the board")
    void addTile_validTile_addsTileToBoard() {
      board.addTile(mockTile1);

      assertEquals(mockTile1, board.getTile(1));
    }

    @Test
    @DisplayName("getTile with non-existent ID should return null")
    void getTile_nonExistentId_returnsNull() {
      assertNull(board.getTile(1));
    }

    @Test
    @DisplayName("getTiles should return all tiles")
    void getTiles_boardWithTiles_returnsAllTiles() {
      board.addTile(mockTile1);
      board.addTile(mockTile2);

      Map<Integer, Tile> tiles = board.getTiles();

      assertEquals(2, tiles.size());
      assertTrue(tiles.containsKey(1));
      assertTrue(tiles.containsKey(2));
      assertEquals(mockTile1, tiles.get(1));
      assertEquals(mockTile2, tiles.get(2));
    }
  }

  @Nested
  @DisplayName("Board Properties Tests")
  class BoardPropertiesTests {

    @Test
    @DisplayName("Board properties should be set correctly")
    void getProperties_newBoard_returnsCorrectValues() {
      assertEquals("Test Board", board.getName());
      assertEquals("A test board", board.getDescription());
      assertEquals(10, board.getRows());
      assertEquals(9, board.getColumns());
      assertEquals(0, board.getStartTileId());
      assertEquals(90, board.getEndTileId());
    }
  }
}
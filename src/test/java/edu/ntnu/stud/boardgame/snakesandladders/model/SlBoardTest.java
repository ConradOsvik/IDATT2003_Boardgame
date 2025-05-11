//package edu.ntnu.stud.boardgame.snakesandladders.model;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import edu.ntnu.stud.boardgame.core.model.Tile;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//class SlBoardTest {
//
//  private SlBoard board;
//
//  @BeforeEach
//  void setUp() {
//    board = new SlBoard();
//    board.initializeBoard();
//  }
//
//  @Test
//  void initializeBoard_createsCorrectNumberOfTiles() {
//    assertEquals(SlBoard.NUM_TILES + 1, board.getTiles().size());
//  }
//
//  @Test
//  void initializeBoard_tilesHaveConnections() {
//    Tile tile1 = board.getTile(1);
//    assertNotNull(tile1);
//    assertEquals(1, tile1.getConnectedTiles().size());
//    assertEquals(2, tile1.getConnectedTiles().getFirst().getTileId());
//  }
//
//  @Test
//  void addLadder_validPositions_createsLadder() {
//    board.addLadder(3, 20);
//
//    assertTrue(board.getLadders().containsKey(3));
//    assertEquals(20, board.getLadders().get(3).intValue());
//
//    Tile startTile = board.getTile(3);
//    assertNotNull(startTile.getLandAction());
//
//    SlPlayer player = new SlPlayer("Test", javafx.scene.paint.Color.RED);
//    player.placeOnTile(startTile);
//
//    assertEquals(20, player.getCurrentTile().getTileId());
//  }
//
//  @Test
//  void addSnake_validPositions_createsSnake() {
//    board.addSnake(20, 3);
//
//    assertTrue(board.getSnakes().containsKey(20));
//    assertEquals(3, board.getSnakes().get(20).intValue());
//
//    Tile headTile = board.getTile(20);
//    assertNotNull(headTile.getLandAction());
//
//    SlPlayer player = new SlPlayer("Test", javafx.scene.paint.Color.RED);
//    player.placeOnTile(headTile);
//
//    assertEquals(3, player.getCurrentTile().getTileId());
//  }
//
//  @Test
//  void getStartingTile_returnsZeroTile() {
//    Tile startingTile = board.getStartingTile();
//    assertNotNull(startingTile);
//    assertEquals(0, startingTile.getTileId());
//  }
//
//  @Test
//  void isLastTile_lastTile_returnsTrue() {
//    Tile lastTile = board.getTile(SlBoard.NUM_TILES);
//    assertTrue(board.isLastTile(lastTile));
//  }
//
//  @Test
//  void isLastTile_notLastTile_returnsFalse() {
//    Tile notLastTile = board.getTile(SlBoard.NUM_TILES - 1);
//    assertFalse(board.isLastTile(notLastTile));
//  }
//
//  @Test
//  void isLastTile_nullTile_returnsFalse() {
//    assertFalse(board.isLastTile(null));
//  }
//
//  @Test
//  void getRows_returnsCorrectNumber() {
//    assertEquals(SlBoard.BOARD_ROWS, board.getRows());
//  }
//
//  @Test
//  void getColumns_returnsCorrectNumber() {
//    assertEquals(SlBoard.BOARD_COLUMNS, board.getColumns());
//  }
//
//  @Test
//  void getTileCoordinates_validTileId_returnsCorrectCoordinates() {
//    int[] coords = board.getTileCoordinates(1);
//    assertEquals(2, coords.length);
//    assertEquals(9, coords[0]);
//    assertEquals(0, coords[1]);
//
//    coords = board.getTileCoordinates(46);
//    assertEquals(2, coords.length);
//    assertTrue(coords[0] >= 0 && coords[0] < SlBoard.BOARD_ROWS);
//    assertTrue(coords[1] >= 0 && coords[1] < SlBoard.BOARD_COLUMNS);
//  }
//
//  @Test
//  void getTileCoordinates_zeroTileId_throwsException() {
//    assertThrows(IllegalArgumentException.class, () -> board.getTileCoordinates(0));
//  }
//
//  @Test
//  void getTileCoordinates_negativeTileId_throwsException() {
//    assertThrows(IllegalArgumentException.class, () -> board.getTileCoordinates(-1));
//  }
//
//  @Test
//  void getTileCoordinates_tooLargeTileId_throwsException() {
//    assertThrows(IllegalArgumentException.class,
//        () -> board.getTileCoordinates(SlBoard.NUM_TILES + 1));
//  }
//}
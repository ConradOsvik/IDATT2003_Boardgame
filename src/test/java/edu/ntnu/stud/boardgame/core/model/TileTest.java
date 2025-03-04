package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for {@link Tile}
 */
@ExtendWith(MockitoExtension.class)
class TileTest {

  private Tile tile;

  @Mock
  private Player player;

  @BeforeEach
  void setup() {
    tile = new Tile(1);
  }

  @Test
  void constructor_validTileId_createsTile() {
    assertEquals(1, tile.getTileId());
  }

  @Test
  void landPlayer_validPlayer_performsLandAction() {
    assertDoesNotThrow(() -> tile.landPlayer(player));
  }

  @Test
  void landPlayer_nullPlayer_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tile.landPlayer(null));
  }

  @Test
  void leavePlayer_validPlayer_performsLeaveAction() {
    assertDoesNotThrow(() -> tile.leavePlayer(player));
  }

  @Test
  void leavePlayer_nullPlayer_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tile.leavePlayer(null));
  }

  @Test
  void addNextTile_validTile_addsToForwardConnections() {
    Tile nextTile = new Tile(2);
    tile.addNextTile(nextTile);

    assertTrue(tile.getConnectedTiles(Tile.Direction.FORWARD).contains(nextTile));
  }

  @Test
  void addNextTile_validTile_addsToBackwardConnectionsOfNextTile() {
    Tile nextTile = new Tile(2);
    tile.addNextTile(nextTile);

    assertTrue(nextTile.getConnectedTiles(Tile.Direction.BACKWARD).contains(tile));
  }

  @Test
  void addNextTile_nullTile_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> tile.addNextTile(null));
  }

  @Test
  void addNextTile_duplicateTile_doesNotAddDuplicate() {
    Tile nextTile = new Tile(2);
    tile.addNextTile(nextTile);
    tile.addNextTile(nextTile);

    assertEquals(1, tile.getConnectedTiles(Tile.Direction.FORWARD).size());
  }

  @Test
  void getConnectedTiles_validDirection_returnsConnectedTiles() {
    Tile nextTile = new Tile(2);
    tile.addNextTile(nextTile);

    assertEquals(1, tile.getConnectedTiles(Tile.Direction.FORWARD).size());
    assertEquals(nextTile, tile.getConnectedTiles(Tile.Direction.FORWARD).get(0));
  }

  @Test
  void equals_sameTileId_returnsTrue() {
    Tile anotherTile = new Tile(1);
    assertEquals(tile, anotherTile);
  }

  @Test
  void equals_differentTileId_returnsFalse() {
    Tile anotherTile = new Tile(2);
    assertNotEquals(tile, anotherTile);
  }

  @Test
  void hashCode_sameTileId_returnsSameHashCode() {
    Tile anotherTile = new Tile(1);
    assertEquals(tile.hashCode(), anotherTile.hashCode());
  }

  @Test
  void toString_returnsStringWithTileId() {
    String tileString = tile.toString();
    assertTrue(tileString.contains("id=1"));
  }
}

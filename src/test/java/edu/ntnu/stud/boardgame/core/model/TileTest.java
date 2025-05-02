package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.stud.boardgame.core.model.action.TileAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TileTest {

  private Tile tile;
  private final int TILE_ID = 5;

  @BeforeEach
  void setUp() {
    tile = new Tile(TILE_ID);
  }

  @Test
  void getTileId_returnsCorrectId() {
    assertEquals(TILE_ID, tile.getTileId());
  }

  @Test
  void setLandAction_validAction_storesAction() {
    TileAction action = Mockito.mock(TileAction.class);
    tile.setLandAction(action);
    assertEquals(action, tile.getLandAction());
  }

  @Test
  void setLandAction_nullAction_removesAction() {
    TileAction action = Mockito.mock(TileAction.class);
    tile.setLandAction(action);
    tile.setLandAction(null);
    assertNull(tile.getLandAction());
  }

  @Test
  void landPlayer_validPlayer_performsAction() {
    Player player = Mockito.mock(Player.class);
    TileAction action = Mockito.mock(TileAction.class);

    tile.setLandAction(action);
    tile.landPlayer(player);

    Mockito.verify(action).perform(player);
  }

  @Test
  void landPlayer_nullPlayer_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> tile.landPlayer(null));
  }

  @Test
  void landPlayer_noAction_doesNotThrow() {
    Player player = Mockito.mock(Player.class);
    assertDoesNotThrow(() -> tile.landPlayer(player));
  }

  @Test
  void leavePlayer_nullPlayer_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> tile.leavePlayer(null));
  }

  @Test
  void leavePlayer_validPlayer_doesNotThrow() {
    Player player = Mockito.mock(Player.class);
    assertDoesNotThrow(() -> tile.leavePlayer(player));
  }

  @Test
  void addConnectedTile_validTile_addsToConnections() {
    Tile connectedTile = new Tile(TILE_ID + 1);
    tile.addConnectedTile(connectedTile);

    assertTrue(tile.getConnectedTiles().contains(connectedTile));
    assertEquals(1, tile.getConnectedTiles().size());
  }

  @Test
  void addConnectedTile_nullTile_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> tile.addConnectedTile(null));
  }

  @Test
  void getConnectedTiles_noConnections_returnsEmptyList() {
    assertTrue(tile.getConnectedTiles().isEmpty());
  }

  @Test
  void equals_sameTileId_returnsTrue() {
    Tile otherTile = new Tile(TILE_ID);
    assertEquals(tile, otherTile);
  }

  @Test
  void equals_differentTileId_returnsFalse() {
    Tile otherTile = new Tile(TILE_ID + 1);
    assertNotEquals(tile, otherTile);
  }

  @Test
  void hashCode_sameTileId_returnsSameHashCode() {
    Tile otherTile = new Tile(TILE_ID);
    assertEquals(tile.hashCode(), otherTile.hashCode());
  }
}
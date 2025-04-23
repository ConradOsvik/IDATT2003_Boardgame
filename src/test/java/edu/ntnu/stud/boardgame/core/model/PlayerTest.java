package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for {@link Player}
 */
@ExtendWith(MockitoExtension.class)
class PlayerTest {

  private Player player;

  @Mock
  private _BoardGame boardGame;

  @Mock
  private Tile currentTile;

  @Mock
  private Tile nextTile;

  @BeforeEach
  void setup() {
    player = new Player("Player", "Token", boardGame);
  }

  @Test
  void constructor_validNameAndGame_createsPlayer() {
    assertEquals("Player", player.getName());
  }

  @Test
  void placeOnTile_validTile_updatesCurrentTile() {
    player.placeOnTile(currentTile);
    assertEquals(currentTile, player.getCurrentTile());
  }

  @Test
  void placeOnTile_nullTile_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> player.placeOnTile(null));
  }

  @Test
  void placeOnTile_validTile_callsLandPlayer() {
    player.placeOnTile(currentTile);
    verify(currentTile).landPlayer(player);
  }

  @Test
  void placeOnTile_validTileAfterPreviousTile_callsLeavePlayerOnPreviousTile() {
    player.placeOnTile(currentTile);

    player.placeOnTile(nextTile);

    verify(currentTile).leavePlayer(player);
  }

  @Test
  void move_zeroSteps_doesNotMove() {
    player.placeOnTile(currentTile);
    player.move(Tile.Direction.FORWARD, 0);

    assertEquals(currentTile, player.getCurrentTile());
  }

  @Test
  void move_negativeSteps_doesNotMove() {
    player.placeOnTile(currentTile);
    player.move(Tile.Direction.FORWARD, -1);

    assertEquals(currentTile, player.getCurrentTile());
  }

  @Test
  void move_nullCurrentTile_doesNotThrowException() {
    assertDoesNotThrow(() -> player.move(Tile.Direction.FORWARD, 1));
  }

  @Test
  void move_validStepsWithConnectedTiles_movesToCorrectTile() {
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);
    Tile tile3 = new Tile(3);

    tile1.addNextTile(tile2);
    tile2.addNextTile(tile3);

    player.placeOnTile(tile1);

    player.move(Tile.Direction.FORWARD, 2);

    assertEquals(tile3, player.getCurrentTile());
  }

  @Test
  void move_validStepsWithNoMoreConnectedTiles_movesToLastPossibleTile() {
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);

    tile1.addNextTile(tile2);

    player.placeOnTile(tile1);

    player.move(Tile.Direction.FORWARD, 3);

    assertEquals(tile2, player.getCurrentTile());
  }

  @Test
  void move_toTileIdNinety_stopsAtTileNinety() {
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(90);
    Tile tile3 = new Tile(3);

    tile1.addNextTile(tile2);
    tile2.addNextTile(tile3);

    player.placeOnTile(tile1);

    player.move(Tile.Direction.FORWARD, 3);

    assertEquals(tile2, player.getCurrentTile());
  }

  @Test
  void getCurrentTile_beforePlacement_returnsNull() {
    assertNull(player.getCurrentTile());
  }

  @Test
  void getName_returnsPlayerName() {
    assertEquals("Player", player.getName());
  }
}
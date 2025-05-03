package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.ntnu.stud.boardgame.core.exception.InvalidMoveException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PlayerTest {

  private TestPlayer player;
  private final String PLAYER_NAME = "TestPlayer";

  private static class TestPlayer extends Player {

    public TestPlayer(String name) {
      super(name);
    }
  }

  @BeforeEach
  void setUp() {
    player = new TestPlayer(PLAYER_NAME);
  }

  @Test
  void constructor_validName_createsPlayer() {
    assertEquals(PLAYER_NAME, player.getName());
    assertNull(player.getCurrentTile());
  }

  @Test
  void constructor_nullName_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new TestPlayer(null));
  }

  @Test
  void constructor_emptyName_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new TestPlayer(""));
  }

  @Test
  void setStartingTile_validTile_setsTile() {
    Tile tile = new Tile(1);
    player.setStartingTile(tile);
    assertEquals(tile, player.getCurrentTile());
  }

  @Test
  void setStartingTile_nullTile_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> player.setStartingTile(null));
  }

  @Test
  void placeOnTile_validTile_placePlayerOnTile() {
    Tile tile = Mockito.mock(Tile.class);
    player.placeOnTile(tile);

    assertEquals(tile, player.getCurrentTile());
    Mockito.verify(tile).landPlayer(player);
  }

  @Test
  void placeOnTile_validTileAfterPrevious_leavesOldTile() {
    Tile oldTile = Mockito.mock(Tile.class);
    Tile newTile = Mockito.mock(Tile.class);

    player.placeOnTile(oldTile);
    player.placeOnTile(newTile);

    Mockito.verify(oldTile).leavePlayer(player);
    Mockito.verify(newTile).landPlayer(player);
    assertEquals(newTile, player.getCurrentTile());
  }

  @Test
  void placeOnTile_nullTile_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> player.placeOnTile(null));
  }

  @Test
  void move_negativeSteps_throwsException() {
    Tile tile = Mockito.mock(Tile.class);
    player.placeOnTile(tile);

    assertThrows(InvalidMoveException.class, () -> player.move(-1));
  }

  @Test
  void move_noCurrentTile_throwsException() {
    assertThrows(InvalidMoveException.class, () -> player.move(1));
  }

  @Test
  void move_zeroSteps_doesNotMove() {
    Tile tile = Mockito.mock(Tile.class);
    player.placeOnTile(tile);

    player.move(0);

    assertEquals(tile, player.getCurrentTile());
  }

  @Test
  void move_validSteps_movesToCorrectTile() {
    Tile startTile = new Tile(1);
    Tile middleTile = new Tile(2);
    Tile endTile = new Tile(3);

    startTile.addConnectedTile(middleTile);
    middleTile.addConnectedTile(endTile);

    player.placeOnTile(startTile);
    player.move(2);

    assertEquals(endTile, player.getCurrentTile());
  }

  @Test
  void move_stepsExceedConnections_movesToLastConnectedTile() {
    Tile startTile = new Tile(1);
    Tile endTile = new Tile(2);

    startTile.addConnectedTile(endTile);

    player.placeOnTile(startTile);
    player.move(5);

    assertEquals(endTile, player.getCurrentTile());
  }

  @Test
  void move_withListOfSteps_followsPath() {
    Tile tile1 = new Tile(1);
    Tile tile2 = new Tile(2);
    Tile tile3 = new Tile(3);

    tile1.addConnectedTile(tile2);
    tile2.addConnectedTile(tile3);

    player.placeOnTile(tile1);
    player.move(List.of(0, 0));

    assertEquals(tile3, player.getCurrentTile());
  }

  @Test
  void equals_sameName_returnsTrue() {
    Player otherPlayer = new TestPlayer(PLAYER_NAME);
    assertEquals(player, otherPlayer);
  }

  @Test
  void equals_differentName_returnsFalse() {
    Player otherPlayer = new TestPlayer("OtherPlayer");
    assertNotEquals(player, otherPlayer);
  }

  @Test
  void hashCode_sameName_returnsSameHashCode() {
    Player otherPlayer = new TestPlayer(PLAYER_NAME);
    assertEquals(player.hashCode(), otherPlayer.hashCode());
  }
}
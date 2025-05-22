package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerMovedEventTest {

  private final int steps = 3;
  private Player mockPlayer;
  private Tile mockFromTile;
  private Tile mockToTile;
  private Board mockBoard;

  @BeforeEach
  void setUp() {
    mockPlayer = mock(Player.class);
    mockFromTile = mock(Tile.class);
    mockToTile = mock(Tile.class);
    mockBoard = mock(Board.class);
  }

  @Test
  void constructor_withValidArguments_shouldCreateEventWithPlayerMovedType() {
    PlayerMovedEvent event =
        new PlayerMovedEvent(mockPlayer, mockFromTile, mockToTile, steps, mockBoard);
    assertNotNull(event);
    assertEquals(EventType.PLAYER_MOVED, event.getEventType());
    assertEquals(mockPlayer, event.getPlayer());
    assertEquals(mockFromTile, event.getFromTile());
    assertEquals(mockToTile, event.getToTile());
    assertEquals(steps, event.getSteps());
    assertEquals(mockBoard, event.getBoard());
  }

  @Test
  void constructor_withSpecificTypeAndValidArguments_shouldCreateEventWithGivenType() {
    EventType specificType = EventType.LADDER_CLIMBED;
    PlayerMovedEvent event =
        new PlayerMovedEvent(specificType, mockPlayer, mockFromTile, mockToTile, steps, mockBoard);
    assertNotNull(event);
    assertEquals(specificType, event.getEventType());
    assertEquals(mockPlayer, event.getPlayer());
    assertEquals(mockFromTile, event.getFromTile());
    assertEquals(mockToTile, event.getToTile());
    assertEquals(steps, event.getSteps());
    assertEquals(mockBoard, event.getBoard());
  }

  @Test
  void constructor_withNullPlayer_shouldThrowIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new PlayerMovedEvent(null, mockFromTile, mockToTile, steps, mockBoard);
        },
        "Player cannot be null for PlayerMovedEvent.");
  }

  @Test
  void constructor_withNullFromTile_shouldThrowIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new PlayerMovedEvent(mockPlayer, null, mockToTile, steps, mockBoard);
        },
        "FromTile cannot be null for PlayerMovedEvent.");
  }

  @Test
  void constructor_withNullToTile_shouldThrowIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new PlayerMovedEvent(mockPlayer, mockFromTile, null, steps, mockBoard);
        },
        "ToTile cannot be null for PlayerMovedEvent.");
  }

  @Test
  void constructor_withNullBoard_shouldThrowIllegalArgumentException() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          new PlayerMovedEvent(mockPlayer, mockFromTile, mockToTile, steps, null);
        },
        "Board cannot be null for PlayerMovedEvent.");
  }

  @Test
  void getPlayer_shouldReturnCorrectPlayer() {
    PlayerMovedEvent event =
        new PlayerMovedEvent(mockPlayer, mockFromTile, mockToTile, steps, mockBoard);
    assertEquals(mockPlayer, event.getPlayer());
  }

  @Test
  void getFromTile_shouldReturnCorrectTile() {
    PlayerMovedEvent event =
        new PlayerMovedEvent(mockPlayer, mockFromTile, mockToTile, steps, mockBoard);
    assertEquals(mockFromTile, event.getFromTile());
  }

  @Test
  void getToTile_shouldReturnCorrectTile() {
    PlayerMovedEvent event =
        new PlayerMovedEvent(mockPlayer, mockFromTile, mockToTile, steps, mockBoard);
    assertEquals(mockToTile, event.getToTile());
  }

  @Test
  void getSteps_shouldReturnCorrectSteps() {
    PlayerMovedEvent event =
        new PlayerMovedEvent(mockPlayer, mockFromTile, mockToTile, steps, mockBoard);
    assertEquals(steps, event.getSteps());
  }

  @Test
  void getBoard_shouldReturnCorrectBoard() {
    PlayerMovedEvent event =
        new PlayerMovedEvent(mockPlayer, mockFromTile, mockToTile, steps, mockBoard);
    assertEquals(mockBoard, event.getBoard());
  }
}

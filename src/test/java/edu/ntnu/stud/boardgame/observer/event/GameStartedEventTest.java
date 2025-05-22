package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameStartedEventTest {

  private Player mockCurrentPlayer;
  private List<Player> mockPlayers;
  private Board mockBoard;
  private Player mockPlayer1;

  @BeforeEach
  void setUp() {
    mockCurrentPlayer = mock(Player.class);
    mockBoard = mock(Board.class);
    mockPlayer1 = mock(Player.class);
    mockPlayers = new ArrayList<>();
    mockPlayers.add(mockPlayer1);
  }

  @Test
  void constructor_withValidArguments_shouldCreateEvent() {
    GameStartedEvent event = new GameStartedEvent(mockCurrentPlayer, mockPlayers, mockBoard);
    assertNotNull(event);
    assertEquals(EventType.GAME_STARTED, event.getEventType());
    assertEquals(mockCurrentPlayer, event.getCurrentPlayer());
    assertEquals(mockPlayers, event.getPlayers());
    assertEquals(mockBoard, event.getBoard());
  }

  @Test
  void constructor_withNullCurrentPlayer_shouldThrowIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new GameStartedEvent(null, mockPlayers, mockBoard);
            });
    assertEquals("CurrentPlayer cannot be null for GameStartedEvent.", exception.getMessage());
  }

  @Test
  void constructor_withNullPlayersList_shouldThrowIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new GameStartedEvent(mockCurrentPlayer, null, mockBoard);
            });
    assertEquals("Players list cannot be null for GameStartedEvent.", exception.getMessage());
  }

  @Test
  void constructor_withNullBoard_shouldThrowIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new GameStartedEvent(mockCurrentPlayer, mockPlayers, null);
            });
    assertEquals("Board cannot be null for GameStartedEvent.", exception.getMessage());
  }

  @Test
  void constructor_withEmptyPlayersList_shouldCreateEvent() {
    List<Player> emptyPlayers = new ArrayList<>();
    GameStartedEvent event = new GameStartedEvent(mockCurrentPlayer, emptyPlayers, mockBoard);
    assertNotNull(event);
    assertEquals(emptyPlayers, event.getPlayers());
  }

  @Test
  void getCurrentPlayer_shouldReturnCorrectPlayer() {
    GameStartedEvent event = new GameStartedEvent(mockCurrentPlayer, mockPlayers, mockBoard);
    assertEquals(mockCurrentPlayer, event.getCurrentPlayer());
  }

  @Test
  void getPlayers_shouldReturnCorrectPlayersList() {
    GameStartedEvent event = new GameStartedEvent(mockCurrentPlayer, mockPlayers, mockBoard);
    assertEquals(mockPlayers, event.getPlayers());
  }

  @Test
  void getBoard_shouldReturnCorrectBoard() {
    GameStartedEvent event = new GameStartedEvent(mockCurrentPlayer, mockPlayers, mockBoard);
    assertEquals(mockBoard, event.getBoard());
  }
}

package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class GameEndedEventTest {

  @Test
  void constructor_withWinner_shouldCreateEvent() {
    Player mockWinner = mock(Player.class);
    GameEndedEvent event = new GameEndedEvent(mockWinner);

    assertNotNull(event);
    assertEquals(EventType.GAME_ENDED, event.getEventType());
    assertEquals(mockWinner, event.getWinner());
  }

  @Test
  void constructor_withNullWinner_shouldCreateEvent() {
    GameEndedEvent event = new GameEndedEvent(null);

    assertNotNull(event);
    assertEquals(EventType.GAME_ENDED, event.getEventType());
    assertNull(event.getWinner(), "Winner can be null if game ends without a single winner.");
  }

  @Test
  void getWinner_shouldReturnCorrectPlayer() {
    Player mockWinner = mock(Player.class);
    GameEndedEvent event = new GameEndedEvent(mockWinner);
    assertEquals(mockWinner, event.getWinner());
  }

  @Test
  void getWinner_whenConstructedWithNull_shouldReturnNull() {
    GameEndedEvent event = new GameEndedEvent(null);
    assertNull(event.getWinner());
  }
}

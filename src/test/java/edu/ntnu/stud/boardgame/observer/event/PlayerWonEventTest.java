package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class PlayerWonEventTest {

  @Test
  void constructor_withValidWinner_shouldCreateEvent() {
    Player mockWinner = mock(Player.class);
    PlayerWonEvent event = new PlayerWonEvent(mockWinner);

    assertNotNull(event);
    assertEquals(EventType.PLAYER_WON, event.getEventType());
    assertEquals(mockWinner, event.getWinner());
  }

  @Test
  void constructor_withNullWinner_shouldStillCreateEventButWinnerIsNull() {
    PlayerWonEvent event = new PlayerWonEvent(null);

    assertNotNull(event);
    assertEquals(EventType.PLAYER_WON, event.getEventType());
    assertNull(event.getWinner(), "Winner can be null if constructor allows it.");
  }

  @Test
  void getWinner_shouldReturnCorrectPlayer() {
    Player mockWinner = mock(Player.class);
    PlayerWonEvent event = new PlayerWonEvent(mockWinner);
    assertEquals(mockWinner, event.getWinner());
  }

  @Test
  void getWinner_whenConstructedWithNull_shouldReturnNull() {
    PlayerWonEvent event = new PlayerWonEvent(null);
    assertNull(event.getWinner());
  }
}

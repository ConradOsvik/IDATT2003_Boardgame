package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class PlayerAddedEventTest {

  @Test
  void constructor_withValidPlayer_shouldCreateEvent() {
    Player mockPlayer = mock(Player.class);
    PlayerAddedEvent event = new PlayerAddedEvent(mockPlayer);

    assertNotNull(event);
    assertEquals(EventType.PLAYER_ADDED, event.getEventType());
    assertEquals(mockPlayer, event.getPlayer());
  }

  @Test
  void constructor_withNullPlayer_shouldThrowIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new PlayerAddedEvent(null);
            });
    assertEquals("Player cannot be null for PlayerAddedEvent.", exception.getMessage());
  }

  @Test
  void getPlayer_shouldReturnCorrectPlayer() {
    Player mockPlayer = mock(Player.class);
    PlayerAddedEvent event = new PlayerAddedEvent(mockPlayer);
    assertEquals(mockPlayer, event.getPlayer());
  }
}

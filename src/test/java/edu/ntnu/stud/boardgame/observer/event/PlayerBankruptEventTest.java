package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class PlayerBankruptEventTest {

  @Test
  void constructor_withValidPlayer_shouldCreateEvent() {
    Player mockPlayer = mock(Player.class);
    PlayerBankruptEvent event = new PlayerBankruptEvent(mockPlayer);

    assertNotNull(event);
    assertEquals(EventType.PLAYER_BANKRUPT, event.getEventType());
    assertEquals(mockPlayer, event.getPlayer());
  }

  @Test
  void constructor_withNullPlayer_shouldThrowIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new PlayerBankruptEvent(null);
            });
    assertEquals("Player cannot be null for PlayerBankruptEvent.", exception.getMessage());
  }

  @Test
  void getPlayer_shouldReturnCorrectPlayer() {
    Player mockPlayer = mock(Player.class);
    PlayerBankruptEvent event = new PlayerBankruptEvent(mockPlayer);
    assertEquals(mockPlayer, event.getPlayer());
  }
}

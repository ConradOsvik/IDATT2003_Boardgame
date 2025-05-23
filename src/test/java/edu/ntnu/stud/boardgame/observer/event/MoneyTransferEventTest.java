package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class MoneyTransferEventTest {

  private final int amount = 100;
  private final String reason = "Rent payment";

  @Test
  void constructor_withValidArguments_shouldCreateEvent() {
    Player mockFromPlayer = mock(Player.class);
    Player mockToPlayer = mock(Player.class);
    MoneyTransferEvent event = new MoneyTransferEvent(mockFromPlayer, mockToPlayer, amount, reason);

    assertNotNull(event);
    assertEquals(EventType.MONEY_TRANSFER, event.getEventType());
    assertEquals(mockFromPlayer, event.getFromPlayer());
    assertEquals(mockToPlayer, event.getToPlayer());
    assertEquals(amount, event.getAmount());
    assertEquals(reason, event.getReason());
  }

  @Test
  void constructor_withNullFromPlayer_shouldCreateEvent() {
    Player mockToPlayer = mock(Player.class);
    MoneyTransferEvent event = new MoneyTransferEvent(null, mockToPlayer, amount, reason);
    assertNotNull(event);
    assertNull(event.getFromPlayer());
    assertEquals(mockToPlayer, event.getToPlayer());
  }

  @Test
  void constructor_withNullToPlayer_shouldCreateEvent() {
    Player mockFromPlayer = mock(Player.class);
    MoneyTransferEvent event = new MoneyTransferEvent(mockFromPlayer, null, amount, reason);
    assertNotNull(event);
    assertEquals(mockFromPlayer, event.getFromPlayer());
    assertNull(event.getToPlayer());
  }

  @Test
  void constructor_withNullReason_shouldThrowIllegalArgumentException() {
    Player mockFromPlayer = mock(Player.class);
    Player mockToPlayer = mock(Player.class);
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new MoneyTransferEvent(mockFromPlayer, mockToPlayer, amount, null);
            });
    assertEquals("Reason for money transfer cannot be null or empty.", exception.getMessage());
  }

  @Test
  void constructor_withEmptyReason_shouldThrowIllegalArgumentException() {
    Player mockFromPlayer = mock(Player.class);
    Player mockToPlayer = mock(Player.class);
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new MoneyTransferEvent(mockFromPlayer, mockToPlayer, amount, "   ");
            });
    assertEquals("Reason for money transfer cannot be null or empty.", exception.getMessage());
  }

  @Test
  void getFromPlayer_shouldReturnCorrectPlayer() {
    Player mockFromPlayer = mock(Player.class);
    MoneyTransferEvent event = new MoneyTransferEvent(mockFromPlayer, null, amount, reason);
    assertEquals(mockFromPlayer, event.getFromPlayer());
  }

  @Test
  void getToPlayer_shouldReturnCorrectPlayer() {
    Player mockToPlayer = mock(Player.class);
    MoneyTransferEvent event = new MoneyTransferEvent(null, mockToPlayer, amount, reason);
    assertEquals(mockToPlayer, event.getToPlayer());
  }

  @Test
  void getAmount_shouldReturnCorrectAmount() {
    MoneyTransferEvent event = new MoneyTransferEvent(null, null, amount, reason);
    assertEquals(amount, event.getAmount());
  }

  @Test
  void getReason_shouldReturnCorrectReason() {
    MoneyTransferEvent event = new MoneyTransferEvent(null, null, amount, reason);
    assertEquals(reason, event.getReason());
  }
}

package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class DiceRolledEventTest {

  @Test
  void constructor_withValidArguments_shouldCreateEvent() {
    Player mockPlayer = mock(Player.class);
    int diceValue = 5;
    DiceRolledEvent event = new DiceRolledEvent(diceValue, mockPlayer);

    assertNotNull(event);
    assertEquals(EventType.DICE_ROLLED, event.getEventType());
    assertEquals(diceValue, event.getDiceValue());
    assertEquals(mockPlayer, event.getCurrentPlayer());
  }

  @Test
  void constructor_withNullPlayer_shouldThrowIllegalArgumentException() {
    int diceValue = 3;
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new DiceRolledEvent(diceValue, null);
            });
    assertEquals("CurrentPlayer cannot be null for DiceRolledEvent.", exception.getMessage());
  }

  @Test
  void getDiceValue_shouldReturnCorrectValue() {
    Player mockPlayer = mock(Player.class);
    int expectedDiceValue = 4;
    DiceRolledEvent event = new DiceRolledEvent(expectedDiceValue, mockPlayer);
    assertEquals(expectedDiceValue, event.getDiceValue());
  }

  @Test
  void getCurrentPlayer_shouldReturnCorrectPlayer() {
    Player mockPlayer = mock(Player.class);
    int diceValue = 6;
    DiceRolledEvent event = new DiceRolledEvent(diceValue, mockPlayer);
    assertEquals(mockPlayer, event.getCurrentPlayer());
  }
}

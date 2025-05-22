package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class TurnChangedEventTest {

    @Test
    void constructor_withValidPlayer_shouldCreateEvent() {
        Player mockPlayer = mock(Player.class);
        TurnChangedEvent event = new TurnChangedEvent(mockPlayer);

        assertNotNull(event);
        assertEquals(EventType.TURN_CHANGED, event.getEventType());
        assertEquals(mockPlayer, event.getCurrentPlayer());
    }

    @Test
    void constructor_withNullPlayer_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new TurnChangedEvent(null);
        });
        assertEquals("CurrentPlayer cannot be null for TurnChangedEvent.", exception.getMessage());
    }

    @Test
    void getCurrentPlayer_shouldReturnCorrectPlayer() {
        Player mockPlayer = mock(Player.class);
        TurnChangedEvent event = new TurnChangedEvent(mockPlayer);
        assertEquals(mockPlayer, event.getCurrentPlayer());
    }
}
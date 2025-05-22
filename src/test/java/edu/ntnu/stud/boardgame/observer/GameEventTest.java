package edu.ntnu.stud.boardgame.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class GameEventTest {

    private static class ConcreteGameEvent extends GameEvent {
        protected ConcreteGameEvent(EventType type) {
            super(type);
        }
    }

    @Test
    void constructor_withNullEventType_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ConcreteGameEvent(null);
        });
        assertEquals("EventType cannot be null for GameEvent.", exception.getMessage());
    }

    @Test
    void constructor_withValidEventType_shouldCreateEvent() {
        EventType expectedType = EventType.GAME_STARTED;
        ConcreteGameEvent event = new ConcreteGameEvent(expectedType);
        assertNotNull(event);
        assertEquals(expectedType, event.getEventType());
    }

    @Test
    void getEventType_shouldReturnCorrectType() {
        EventType expectedType = EventType.DICE_ROLLED;
        ConcreteGameEvent event = new ConcreteGameEvent(expectedType);
        assertEquals(expectedType, event.getEventType());
    }

    @Test
    void eventTypeEnum_values_shouldBeAccessible() {
        assertNotNull(EventType.valueOf("GAME_CREATED"));
        assertNotNull(EventType.values());
        assertTrue(EventType.values().length > 0);
    }
}
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

class GameCreatedEventTest {

    private Board mockBoard;
    private List<Player> mockPlayers;
    private Player mockPlayer1;

    @BeforeEach
    void setUp() {
        mockBoard = mock(Board.class);
        mockPlayer1 = mock(Player.class);
        mockPlayers = new ArrayList<>();
        mockPlayers.add(mockPlayer1);
    }

    @Test
    void constructor_withValidArguments_shouldCreateEvent() {
        GameCreatedEvent event = new GameCreatedEvent(mockBoard, mockPlayers);
        assertNotNull(event);
        assertEquals(EventType.GAME_CREATED, event.getEventType());
        assertEquals(mockBoard, event.getBoard());
        assertEquals(mockPlayers, event.getPlayers());
    }

    @Test
    void constructor_withNullBoard_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new GameCreatedEvent(null, mockPlayers);
        });
        assertEquals("Board cannot be null for GameCreatedEvent.", exception.getMessage());
    }

    @Test
    void constructor_withNullPlayersList_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new GameCreatedEvent(mockBoard, null);
        });
        assertEquals("Players list cannot be null for GameCreatedEvent.", exception.getMessage());
    }

    @Test
    void constructor_withEmptyPlayersList_shouldCreateEvent() {
        List<Player> emptyPlayers = new ArrayList<>();
        GameCreatedEvent event = new GameCreatedEvent(mockBoard, emptyPlayers);
        assertNotNull(event);
        assertEquals(EventType.GAME_CREATED, event.getEventType());
        assertEquals(mockBoard, event.getBoard());
        assertEquals(emptyPlayers, event.getPlayers());
        assertNotNull(event.getPlayers(), "Players list should not be null even if empty.");
    }

    @Test
    void getBoard_shouldReturnCorrectBoard() {
        GameCreatedEvent event = new GameCreatedEvent(mockBoard, mockPlayers);
        assertEquals(mockBoard, event.getBoard());
    }

    @Test
    void getPlayers_shouldReturnCorrectPlayersList() {
        GameCreatedEvent event = new GameCreatedEvent(mockBoard, mockPlayers);
        assertEquals(mockPlayers, event.getPlayers());
    }
}
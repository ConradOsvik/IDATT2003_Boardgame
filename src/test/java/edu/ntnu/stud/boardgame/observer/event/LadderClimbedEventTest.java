package edu.ntnu.stud.boardgame.observer.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent.EventType;
import org.junit.jupiter.api.Test;

class LadderClimbedEventTest {

    @Test
    void constructor_shouldSetCorrectEventTypeAndPassArgumentsToSuper() {
        Player mockPlayer = mock(Player.class);
        Tile mockFromTile = mock(Tile.class);
        Tile mockToTile = mock(Tile.class);
        Board mockBoard = mock(Board.class);
        int steps = 5;

        LadderClimbedEvent event = new LadderClimbedEvent(mockPlayer, mockFromTile, mockToTile, steps, mockBoard);

        assertNotNull(event);
        assertEquals(EventType.LADDER_CLIMBED, event.getEventType());
        assertEquals(mockPlayer, event.getPlayer());
        assertEquals(mockFromTile, event.getFromTile());
        assertEquals(mockToTile, event.getToTile());
        assertEquals(steps, event.getSteps());
        assertEquals(mockBoard, event.getBoard());
    }
}
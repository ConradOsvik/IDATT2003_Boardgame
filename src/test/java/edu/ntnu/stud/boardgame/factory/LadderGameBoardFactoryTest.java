package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LadderGameBoardFactoryTest {

    @Test
    void getAvailableBoards_returnsCorrectList() {
        List<String> availableBoards = LadderGameBoardFactory.getAvailableBoards();
        assertNotNull(availableBoards);
        assertEquals(5, availableBoards.size());
        assertTrue(availableBoards.containsAll(List.of("Classic", "Easy", "Hard", "Extreme", "Small")));
    }

    @Test
    void createBoard_validNames_returnsBoard() {
        List<String> boardNames = List.of("Classic", "Easy", "Hard", "Extreme", "Small");
        for (String boardName : boardNames) {
            Board board = LadderGameBoardFactory.createBoard(boardName);
            assertNotNull(board, "Board should not be null for name: " + boardName);
            assertEquals(boardName + " Snakes and Ladders", board.getName(),
                    "Board name should match for: " + boardName);
            assertTrue(board.getTiles().size() > 0, "Board should have tiles for: " + boardName);
            // Check for start and end tiles
            assertNotNull(board.getTile(0), "Board should have a starting tile (ID 0).");
            assertNotNull(board.getTile(board.getTiles().size() - 1), "Board should have an ending tile.");
        }
    }

    @Test
    void createBoard_classicBoard_hasSpecificLadderAndSnake() {
        Board classicBoard = LadderGameBoardFactory.createBoard("Classic");
        assertNotNull(classicBoard);
        // Check a known ladder (e.g., from tile 1 to 40)
        Tile tile1 = classicBoard.getTile(1);
        assertNotNull(tile1.getLandAction());
        assertTrue(tile1.getLandAction() instanceof LadderAction);
        assertEquals(classicBoard.getTile(40), ((LadderAction) tile1.getLandAction()).getDestinationTile());

        // Check a known snake (e.g., from tile 24 to 5)
        Tile tile24 = classicBoard.getTile(24);
        assertNotNull(tile24.getLandAction());
        assertTrue(tile24.getLandAction() instanceof SnakeAction);
        assertEquals(classicBoard.getTile(5), ((SnakeAction) tile24.getLandAction()).getDestinationTile());
    }

    @Test
    void createBoard_invalidName_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            LadderGameBoardFactory.createBoard("NonExistentBoard");
        });
        assertEquals("Unknown board name: NonExistentBoard", exception.getMessage());
    }

    @Test
    void createBoard_nullName_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            LadderGameBoardFactory.createBoard(null);
        });
        assertEquals("Board name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createBoard_emptyName_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            LadderGameBoardFactory.createBoard("  ");
        });
        assertEquals("Board name cannot be null or empty.", exception.getMessage());
    }
}
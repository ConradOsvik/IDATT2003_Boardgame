package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.StartAction;
import edu.ntnu.stud.boardgame.model.action.TaxAction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MonopolyBoardFactoryTest {

    @Test
    void getAvailableBoards_returnsCorrectList() {
        List<String> availableBoards = MonopolyBoardFactory.getAvailableBoards();
        assertNotNull(availableBoards);
        assertEquals(3, availableBoards.size());
        assertTrue(availableBoards.containsAll(List.of("Standard", "Mini", "Economic")));
    }

    @Test
    void createBoard_validNames_returnsBoard() {
        List<String> boardNames = MonopolyBoardFactory.getAvailableBoards();
        for (String boardName : boardNames) {
            Board board = MonopolyBoardFactory.createBoard(boardName);
            assertNotNull(board, "Board should not be null for name: " + boardName);
            // Board names in the factory are like "Standard Monopoly", "Mini Monopoly"
            assertTrue(board.getName().startsWith(boardName), "Board name in factory should start with: " + boardName);
            assertTrue(board.getTiles().size() > 0, "Board should have tiles for: " + boardName);
            // Check for start tile (GO, ID 0)
            Tile startTile = board.getTile(0);
            assertNotNull(startTile, "Board should have a starting tile (ID 0).");
            assertEquals("GO", startTile.getName());
            assertTrue(startTile.getLandAction() instanceof StartAction, "Start tile should have StartAction.");
        }
    }

    @Test
    void createBoard_standardBoard_hasCorrectNumberOfTilesAndSpecificActions() {
        Board standardBoard = MonopolyBoardFactory.createBoard("Standard");
        assertNotNull(standardBoard);
        assertEquals(40, standardBoard.getTiles().size()); // Standard board has 40 tiles (0-39)

        // Example: Check tile 5 (a Tax tile in the setup logic)
        Tile tile5 = standardBoard.getTile(5);
        assertNotNull(tile5);
        assertTrue(tile5.getLandAction() instanceof TaxAction, "Tile 5 should be TaxAction");
        assertEquals(100, ((TaxAction) tile5.getLandAction()).getAmount());

        // Example: Check tile 1 (a Property tile)
        Tile tile1 = standardBoard.getTile(1);
        assertNotNull(tile1);
        assertTrue(tile1.getLandAction() instanceof PropertyAction, "Tile 1 should be PropertyAction");
        assertTrue(((PropertyAction) tile1.getLandAction()).getPrice() > 0);
    }

    @Test
    void createBoard_invalidName_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MonopolyBoardFactory.createBoard("NonExistentBoard");
        });
        assertEquals("Unknown board name: NonExistentBoard", exception.getMessage());
    }

    @Test
    void createBoard_nullName_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MonopolyBoardFactory.createBoard(null);
        });
        assertEquals("Board name cannot be null or empty.", exception.getMessage());
    }

    @Test
    void createBoard_emptyName_throwsIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            MonopolyBoardFactory.createBoard("  ");
        });
        assertEquals("Board name cannot be null or empty.", exception.getMessage());
    }
}
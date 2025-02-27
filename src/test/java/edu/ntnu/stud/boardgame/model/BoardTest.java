package edu.ntnu.stud.boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test class for {@link Board}
 */
class BoardTest {
    private Board board;
    private Tile tile;

    @BeforeEach
    void setup() {
        board = new Board();
        tile = new Tile(1);
    }

    @Test
    void addTile_NewTile_AddsToTileMap() {
        board.addTile(tile);
        assertEquals(tile, board.getTile(1));
    }

    @Test
    void getTile_NonexistentId_ReturnsNull() {
        assertNull(board.getTile(999));
    }

    @Test
    void addTile_DuplicateTile_OverwritesExisting() {
        Tile tile2 = new Tile(1);
        board.addTile(tile);
        board.addTile(tile2);
        assertEquals(tile2, board.getTile(1));
    }
}
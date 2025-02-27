package edu.ntnu.stud.boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for {@link Tile}
 */
class TileTest {
    private Tile tile;
    private Player player;

    @BeforeEach
    void setup() {
        tile = new Tile(1);
        player = new Player("P1", new BoardGame());
    }

    @Test
    void landPlayer_NullPlayer_ThrowsException() {
        assertThrows(NullPointerException.class, () -> tile.landPlayer(null));
    }

    @Test
    void setNextTile_ValidTile_UpdatesNextTileReference() {
        Tile newTile = new Tile(2);
        tile.setNextTile(newTile);
        assertEquals(newTile, tile.getNextTile());
    }
}
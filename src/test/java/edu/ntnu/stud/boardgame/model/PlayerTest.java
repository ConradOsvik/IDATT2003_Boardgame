package edu.ntnu.stud.boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link Player}
 */
class PlayerTest {
    private BoardGame game;
    private Player player;
    private Tile tile1;

    @BeforeEach
    void setup() {
        game = new BoardGame();
        game.createBoard();
        player = new Player("TestPlayer", game);
        tile1 = new Tile(1);
        game.getBoard().addTile(tile1);
        player.placeOnTile(tile1);
    }

    @Test
    void move_PositiveSteps_MovesPlayerForward() {
        Tile tile2 = new Tile(2);
        tile1.setNextTile(tile2);
        player.move(1);
        assertEquals(tile2, player.getCurrentTile());
    }

    @Test
    void move_NegativeSteps_NoMovement() {
        player.move(-2);
        assertEquals(tile1, player.getCurrentTile());
    }

    @Test
    void placeOnTile_ValidTile_UpdatesCurrentTile() {
        Tile newTile = new Tile(5);
        player.placeOnTile(newTile);
        assertEquals(newTile, player.getCurrentTile());
    }
}
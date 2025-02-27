package edu.ntnu.stud.boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link BoardGame}
 */
class BoardGameTest {
    private BoardGame game;
    private Player player1;

    @BeforeEach
    void setup() {
        game = new BoardGame();
        game.createBoard();
        game.createDice();
        player1 = new Player("P1", game);
    }

    @Test
    void addPlayer_ValidPlayer_AddsToPlayersList() {
        game.addPlayer(player1);
        assertEquals(1, game.players.size());
    }

    @Test
    void addPlayer_NullPlayer_ThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> game.addPlayer(null));
    }

    @Test
    void getWinner_PlayerOnWinningTile_ReturnsWinner() {
        Tile winningTile = new Tile(90);
        game.getBoard().addTile(winningTile);
        player1.placeOnTile(winningTile);
        game.addPlayer(player1);

        assertEquals(player1, game.getWinner());
    }

    @Test
    void getWinner_NoWinner_ReturnsNull() {
        assertNull(game.getWinner());
    }
}
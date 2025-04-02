package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for {@link BoardGame}
 */
@ExtendWith(MockitoExtension.class)
class BoardGameTest {

  private BoardGame boardGame;

  @Mock
  private Player player;

  @Mock
  private Player player2;

  @BeforeEach
  void setup() {
    boardGame = new BoardGame();
    boardGame.createBoard();
  }

  @Test
  void createBoard_createsBoard() {
    boardGame = new BoardGame();
    boardGame.createBoard();

    assertDoesNotThrow(() -> boardGame.addPlayer(player));
  }

  @Test
  void createDice_withValidNumberOfDice_createsDice() {
    assertDoesNotThrow(() -> boardGame.createDice(2));
  }

  @Test
  void createDice_withInvalidNumberOfDice_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> boardGame.createDice(0));
  }

  @Test
  void addPlayer_validPlayer_addsPlayerToList() {
    boardGame.addPlayer(player);

    assertEquals(1, boardGame.getPlayers().size());
    assertEquals(player, boardGame.getPlayers().getFirst());
  }

  @Test
  void addPlayer_nullPlayer_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> boardGame.addPlayer(null));
  }

  @Test
  void addPlayer_placesPlayerOnFirstTile() {
    Player player = new Player("Player", "Token", boardGame);
    boardGame.addPlayer(player);

    assertEquals(1, player.getCurrentTile().getTileId());
  }

  @Test
  void play_playerReachesLastTile_setsWinnerAndFinishesGame() {
    BoardGame game = new BoardGame();
    game.createBoard();
    game.createDice(1);
    Player player = new Player("Player", "Token", game);

    game.addPlayer(player);
    player.placeOnTile(game.getBoard().getTile(89));
    game.play();

    assertTrue(game.isFinished());
    assertEquals(player, game.getWinner());
  }

  @Test
  void play_noPlayerReachesLastTile_gameNotFinished() {
    boardGame.createDice(1);
    Player player = new Player("Player", "Token1", boardGame);
    Player player2 = new Player("Player2", "Token2", boardGame);

    boardGame.addPlayer(player);
    boardGame.addPlayer(player2);
    boardGame.play();

    assertFalse(boardGame.isFinished());
    assertNull(boardGame.getWinner());
  }

  @Test
  void getPlayers_returnsCorrectList() {
    boardGame.addPlayer(player);
    boardGame.addPlayer(player2);

    assertEquals(2, boardGame.getPlayers().size());
    assertTrue(boardGame.getPlayers().contains(player));
    assertTrue(boardGame.getPlayers().contains(player2));
  }

  @Test
  void getPlayers_returnsDefensiveCopy() {
    boardGame.addPlayer(player);

    boardGame.getPlayers().clear();

    assertEquals(1, boardGame.getPlayers().size());
  }
}
package edu.ntnu.stud.boardgame.snakesandladders.factory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;
import org.junit.jupiter.api.Test;

class SlBoardGameFactoryTest {

  @Test
  void createClassicGame_returnsInitializedGame() {
    SlBoardGame game = SlBoardGameFactory.createClassicGame();
    SlBoard board = (SlBoard) game.getBoard();

    assertNotNull(game);
    assertNotNull(game.getBoard());
    assertInstanceOf(SlBoard.class, game.getBoard());

    assertFalse(board.getLadders().isEmpty());
    assertFalse(board.getSnakes().isEmpty());
  }
}
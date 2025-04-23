package edu.ntnu.stud.boardgame.core.factory;

import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.snakesandladders.model.SLBoardGame;

/**
 * Factory for creating board games.
 */
public class BoardGameFactory {

  /**
   * Creates a new Snakes and Ladders game with default settings.
   * The game is initialized with a board and 2 dice.
   *
   * @return A new Snakes and Ladders game
   */
  public static BoardGame createSnakesAndLaddersGame() {
    BoardGame game = new SLBoardGame();
    game.createBoard();
    game.createDice(2);
    return game;
  }

  /**
   * Creates a new Snakes and Ladders game with the specified number of dice.
   *
   * @param numberOfDice The number of dice to use
   * @return A new Snakes and Ladders game
   */
  public static BoardGame createSnakesAndLaddersGame(int numberOfDice) {
    BoardGame game = new SLBoardGame();
    game.createBoard();
    game.createDice(numberOfDice);
    return game;
  }
}
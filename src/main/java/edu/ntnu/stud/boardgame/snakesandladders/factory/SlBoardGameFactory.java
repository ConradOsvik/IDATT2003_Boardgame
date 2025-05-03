package edu.ntnu.stud.boardgame.snakesandladders.factory;

import edu.ntnu.stud.boardgame.core.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoardGame;

public class SlBoardGameFactory implements BoardGameFactory {

  public static SlBoardGame createClassicGame() {
    SlBoardGame boardGame = new SlBoardGame();
    boardGame.createBoard();
    boardGame.createDice(1);

    SlBoard board = boardGame.getBoard();
    board.addLadder(1, 40);
    board.addLadder(8, 10);
    board.addLadder(36, 52);
    board.addLadder(43, 62);
    board.addLadder(49, 79);
    board.addLadder(65, 82);
    board.addLadder(68, 85);

    board.addSnake(24, 5);
    board.addSnake(33, 3);
    board.addSnake(42, 30);
    board.addSnake(56, 37);
    board.addSnake(64, 27);
    board.addSnake(74, 12);
    board.addSnake(87, 70);

    return boardGame;
  }
}

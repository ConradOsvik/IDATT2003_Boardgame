package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.model.game.LadderGame;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import java.util.List;

public class BoardGameFactory {

  private final BoardFileService boardFileService;

  public BoardGameFactory(BoardFileService boardFileService) {
    this.boardFileService = boardFileService;
  }

  public BoardGame createClassicLadderGame() {
    BoardGame game = new LadderGame();
    game.createBoard();
    game.createDice(2);
    return game;
  }

  public BoardGame loadGameFromFile(BoardGameType type, String fileName) throws BoardFileException {
    Board board = boardFileService.loadBoard(fileName);
    BoardGame game = new LadderGame();
    game.setBoard(board);
    game.createDice(2);

    return game;
  }

  public List<String> getAvailableGameBoards() {
    return boardFileService.listAvailableBoards();
  }
}

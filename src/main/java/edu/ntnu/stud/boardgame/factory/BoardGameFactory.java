package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.model.game.LadderGame;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import java.util.List;

public class BoardGameFactory {

  private final BoardFileService boardFileService;

  public BoardGameFactory(BoardFileService boardFileService) {
    this.boardFileService = boardFileService;
  }

  public BoardGame createGame(BoardGameType type) {
    BoardGame game = switch (type) {
      case LADDER -> new LadderGame();
      case MONOPOLY -> new MonopolyGame();
    };

    game.createBoard();
    game.createDice(2);
    return game;
  }

  public BoardGame loadGameFromFile(BoardGameType type, String fileName) throws BoardFileException {
    Board board = boardFileService.loadBoard(fileName);

    BoardGame game = switch (type) {
      case LADDER -> new LadderGame();
      case MONOPOLY -> new MonopolyGame();
    };

    game.setBoard(board);
    game.createDice(2);

    return game;
  }

  public List<String> getAvailableGameBoards() {
    return boardFileService.listAvailableBoards();
  }
}

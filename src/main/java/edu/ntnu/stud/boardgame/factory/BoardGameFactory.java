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

  public BoardGame loadGameFromFile(BoardGameType type, String fileName) throws BoardFileException {
    BoardGame game = createEmptyGame(type);

    Board board;
    if (fileName.equalsIgnoreCase("Default")) {
      board = game.createDefaultBoard();
    } else {
      board = boardFileService.loadBoard(type, fileName);
    }
    game.setBoard(board);

    game.createDice(2);

    return game;
  }

  public List<String> getAvailableGameBoards(BoardGameType type) {
    List<String> availableGameBoards = boardFileService.listAvailableBoards(type);
    availableGameBoards.addFirst("Default");

    return availableGameBoards;
  }

  private BoardGame createEmptyGame(BoardGameType type) {
    return switch (type) {
      case LADDER -> new LadderGame();
      case MONOPOLY -> new MonopolyGame();
    };
  }
}

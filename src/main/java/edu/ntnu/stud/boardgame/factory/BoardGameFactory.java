package edu.ntnu.stud.boardgame.factory;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.model.game.LadderGame;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BoardGameFactory {

  private static final Logger LOGGER = Logger.getLogger(BoardGameFactory.class.getName());

  private final BoardFileService boardFileService;

  public BoardGameFactory(BoardFileService boardFileService) {
    if (boardFileService == null) {
      throw new IllegalArgumentException("BoardFileService cannot be null.");
    }
    this.boardFileService = boardFileService;
  }

  public BoardGame createGame(BoardGameType type, String boardName) throws BoardFileException {
    if (type == null) {
      throw new IllegalArgumentException("BoardGameType cannot be null.");
    }
    if (boardName == null || boardName.trim().isEmpty()) {
      throw new IllegalArgumentException("Board name cannot be null or empty.");
    }

    Board board;

    if (boardName.startsWith("Predefined:")) {
      String predefinedName = boardName.substring("Predefined:".length());
      board = getPredefinedBoard(type, predefinedName);
    } else {
      board = boardFileService.loadBoard(type, boardName);
    }

    BoardGame game = createEmptyGame(type);
    game.setBoard(board);
    game.createDice(2);

    return game;
  }

  private Board getPredefinedBoard(BoardGameType type, String boardName) {
    return switch (type) {
      case LADDER -> LadderGameBoardFactory.createBoard(boardName);
      case MONOPOLY -> MonopolyBoardFactory.createBoard(boardName);
    };
  }

  public List<String> getAvailableGameBoards(BoardGameType gameType) {
    if (gameType == null) {
      throw new IllegalArgumentException("GameType cannot be null.");
    }
    List<String> result = new ArrayList<>();

    List<String> predefinedBoards = switch (gameType) {
      case LADDER -> LadderGameBoardFactory.getAvailableBoards();
      case MONOPOLY -> MonopolyBoardFactory.getAvailableBoards();
    };

    for (String board : predefinedBoards) {
      result.add("Predefined:" + board);
    }

    List<String> fileBoards = boardFileService.listAvailableBoards(gameType);
    result.addAll(fileBoards);

    return result;
  }

  private BoardGame createEmptyGame(BoardGameType type) {
    if (type == null) {
      throw new IllegalArgumentException("BoardGameType cannot be null for createEmptyGame.");
    }
    return switch (type) {
      case LADDER -> new LadderGame();
      case MONOPOLY -> new MonopolyGame();
    };
  }
}
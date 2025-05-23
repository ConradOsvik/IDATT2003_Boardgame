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

/**
 * Factory responsible for creating and configuring different types of board games.
 *
 * <p>This factory provides functionality to:
 *
 * <ul>
 *   <li>Create board games of different types (Ladder, Monopoly)
 *   <li>Load board configurations from predefined templates or from files
 *   <li>List available board configurations for specific game types
 * </ul>
 *
 * <p>The factory relies on a {@link BoardFileService} to handle loading of board configurations
 * from the file system. Predefined boards are provided through specialized board factories for each
 * game type.
 *
 * @see BoardFileService
 * @see BoardGameType
 * @see BoardGame
 */
public class BoardGameFactory {

  private static final Logger LOGGER = Logger.getLogger(BoardGameFactory.class.getName());

  private final BoardFileService boardFileService;

  /**
   * Constructs a new board game factory with the specified board file service.
   *
   * @param boardFileService the service used to load board configurations from files
   * @throws IllegalArgumentException if boardFileService is null
   */
  public BoardGameFactory(BoardFileService boardFileService) {
    if (boardFileService == null) {
      throw new IllegalArgumentException("BoardFileService cannot be null.");
    }
    this.boardFileService = boardFileService;
  }

  /**
   * Creates and initializes a board game of the specified type with the given board.
   *
   * <p>The board can be either a predefined board (prefixed with "Predefined:") or a custom board
   * loaded from the file system. The method sets up the game with the appropriate board and
   * initializes it with two dice.
   *
   * @param type the type of board game to create
   * @param boardName the name of the board to use, potentially prefixed with "Predefined:"
   * @return a fully initialized board game
   * @throws BoardFileException if there is an error loading the board from files
   * @throws IllegalArgumentException if type is null or boardName is null or empty
   */
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

  /**
   * Retrieves a predefined board template for the specified game type and board name.
   *
   * @param type the type of board game
   * @param boardName the name of the predefined board template
   * @return a board instance matching the specified type and name
   */
  private Board getPredefinedBoard(BoardGameType type, String boardName) {
    return switch (type) {
      case LADDER -> LadderGameBoardFactory.createBoard(boardName);
      case MONOPOLY -> MonopolyBoardFactory.createBoard(boardName);
    };
  }

  /**
   * Retrieves a list of available board configurations for the specified game type.
   *
   * <p>The returned list includes both predefined boards (prefixed with "Predefined:") and custom
   * boards available in the file system.
   *
   * @param gameType the type of board game to get available boards for
   * @return a list of board names, where predefined boards are prefixed with "Predefined:"
   * @throws IllegalArgumentException if gameType is null
   */
  public List<String> getAvailableGameBoards(BoardGameType gameType) {
    if (gameType == null) {
      throw new IllegalArgumentException("GameType cannot be null.");
    }
    List<String> result = new ArrayList<>();

    List<String> predefinedBoards =
        switch (gameType) {
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

  /**
   * Creates an empty game instance of the specified type.
   *
   * <p>The created game will not have a board or any other configurations set.
   *
   * @param type the type of board game to create
   * @return an uninitialized game of the specified type
   * @throws IllegalArgumentException if type is null
   */
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

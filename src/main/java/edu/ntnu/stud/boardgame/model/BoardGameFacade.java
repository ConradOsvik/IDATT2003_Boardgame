package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import java.util.ArrayList;
import java.util.List;

/**
 * Facade providing a simplified interface to the board game system.
 *
 * <p>Coordinates game creation, player management, board operations, and observer notifications.
 * Acts as the main entry point for game operations.
 */
public class BoardGameFacade {

  private final BoardGameFactory factory;
  private final BoardFileService boardFileService;
  private final List<BoardGameObserver> observers;
  private BoardGame currentGame;
  private BoardGameType currentGameType;

  /** Creates a new facade with required services. */
  public BoardGameFacade() {
    this.boardFileService = BoardFileService.getInstance();
    this.factory = new BoardGameFactory(boardFileService);
    this.observers = new ArrayList<>();
  }

  /**
   * Creates a new game with the specified board.
   *
   * @param boardName name of the board to use
   * @throws BoardGameException if game type not selected or creation fails
   */
  public void createGame(String boardName) throws BoardGameException {
    if (currentGameType == null) {
      throw new InvalidGameStateException("No game type has been selected");
    }

    try {
      currentGame = factory.createGame(currentGameType, boardName);
      currentGame.registerObservers(observers);
      currentGame.notifyGameCreated();
    } catch (Exception e) {
      throw new BoardGameException("Failed to create game with board: " + boardName, e);
    }
  }

  /**
   * Saves the current board to a file.
   *
   * @param fileName name of the file to save to
   * @throws BoardGameException if no game exists or save fails
   */
  public void saveCurrentBoard(String fileName) throws BoardGameException {
    if (currentGame == null || currentGame.getBoard() == null) {
      throw new BoardGameException("No game has been created");
    }

    try {
      boardFileService.saveBoard(currentGameType, fileName, currentGame.getBoard());
    } catch (Exception e) {
      throw new BoardGameException("Failed to save board: " + e.getMessage(), e);
    }
  }

  /**
   * Starts the current game.
   *
   * @throws BoardGameException if no game exists or start fails
   */
  public void startGame() throws BoardGameException {
    if (currentGame == null) {
      throw new BoardGameException("No game has been created");
    }

    try {
      currentGame.startGame();
    } catch (IllegalStateException e) {
      throw new BoardGameException("Failed to start game: " + e.getMessage(), e);
    }
  }

  /**
   * Adds a player to the current game.
   *
   * @param name player's name
   * @param pieceType player's piece type
   * @throws BoardGameException if no game exists
   */
  public void addPlayer(String name, PieceType pieceType) throws BoardGameException {
    if (currentGame == null) {
      throw new BoardGameException("No game has been created");
    }

    Player player = new Player(name, pieceType);
    currentGame.addPlayer(player);
  }

  /**
   * Executes a turn in the current game.
   *
   * @throws BoardGameException if no game exists or game is over
   */
  public void playTurn() throws BoardGameException {
    if (currentGame == null) {
      throw new BoardGameException("No game has been created");
    }

    if (currentGame.isGameOver()) {
      throw new BoardGameException("Game is already over");
    }

    currentGame.playTurn();

    if (!currentGame.isGameOver()) {
      currentGame.nextTurn();
    }
  }

  public BoardGame getCurrentGame() {
    return currentGame;
  }

  public BoardGameType getCurrentGameType() {
    return currentGameType;
  }

  public void setCurrentGameType(BoardGameType currentGameType) {
    this.currentGameType = currentGameType;
  }

  /**
   * Gets a list of available board configurations.
   *
   * @return list of board names
   * @throws InvalidGameStateException if no game type selected
   */
  public List<String> getAvailableGameBoards() {
    if (currentGameType == null) {
      throw new InvalidGameStateException("No game type has been selected");
    }

    return factory.getAvailableGameBoards(currentGameType);
  }

  /**
   * Registers an observer for game events.
   *
   * @param observer the observer to register
   */
  public void registerObserver(BoardGameObserver observer) {
    if (observer == null) {
      System.err.println("Attempted to register a null observer in BoardGameFacade.");
      return;
    }
    observers.add(observer);

    if (currentGame != null) {
      currentGame.registerObserver(observer);
    }
  }
}

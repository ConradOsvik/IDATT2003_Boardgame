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

public class BoardGameFacade {

  private final BoardGameFactory factory;
  private final BoardFileService boardFileService;
  private final List<BoardGameObserver> observers;
  private BoardGame currentGame;
  private BoardGameType currentGameType;

  public BoardGameFacade() {
    this.boardFileService = BoardFileService.getInstance();
    this.factory = new BoardGameFactory(boardFileService);
    this.observers = new ArrayList<>();
  }

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

  public void addPlayer(String name, PieceType pieceType) throws BoardGameException {
    if (currentGame == null) {
      throw new BoardGameException("No game has been created");
    }

    Player player = new Player(name, pieceType);
    currentGame.addPlayer(player);
  }

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

  public List<String> getAvailableGameBoards() {
    if (currentGameType == null) {
      throw new InvalidGameStateException("No game type has been selected");
    }

    return factory.getAvailableGameBoards(currentGameType);
  }

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
package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
import edu.ntnu.stud.boardgame.exception.InvalidGameStateException;
import edu.ntnu.stud.boardgame.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.service.BoardFileService;
import java.util.List;

public class BoardGameFacade {

  private final BoardGameFactory factory;
  private BoardGame currentGame;
  private BoardGameType currentGameType;

  public BoardGameFacade(BoardFileService boardFileService) {
    this.factory = new BoardGameFactory(boardFileService);
  }

  public void createGame() {
    if (currentGameType == null) {
      throw new InvalidGameStateException("No game type has been selected");
    }

    currentGame = factory.createGame(currentGameType);
  }

  public void createGame(String fileName) throws BoardGameException {
    if (currentGameType == null) {
      throw new InvalidGameStateException("No game type has been selected");
    }

    try {
      currentGame = factory.loadGameFromFile(currentGameType, fileName);
    } catch (Exception e) {
      throw new BoardGameException("Failed to load game from file: " + fileName, e);
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

  public void restartGame() throws BoardGameException {
    if (currentGame == null) {
      throw new BoardGameException("No game has been created");
    }

    try {
      currentGame.restartGame();
    } catch (IllegalStateException e) {
      throw new BoardGameException("Failed to restart game: " + e.getMessage(), e);
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
    if (currentGame != null) {
      currentGame.registerObserver(observer);
    }
  }

}

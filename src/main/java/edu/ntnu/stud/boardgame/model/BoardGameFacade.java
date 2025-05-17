package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
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

  public BoardGameFacade(BoardFileService boardFileService) {
    this.factory = new BoardGameFactory(boardFileService);
  }

  public void createGame(BoardGameType type) {
    currentGame = factory.createGame(type);
  }

  public void createGame(BoardGameType type, String fileName) throws BoardGameException {
    try {
      currentGame = factory.loadGameFromFile(type, fileName);
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

  public List<String> getAvailableGameBoards() {
    return factory.getAvailableGameBoards();
  }

  public void registerObserver(BoardGameObserver observer) {
    if (currentGame != null) {
      currentGame.registerObserver(observer);
    }
  }

}

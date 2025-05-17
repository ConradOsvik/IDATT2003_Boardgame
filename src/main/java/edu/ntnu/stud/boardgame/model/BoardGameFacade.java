package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
import edu.ntnu.stud.boardgame.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.service.BoardFileService;

public class BoardGameFacade {

  private final BoardGameFactory factory;
  private BoardGame currentGame;

  public BoardGameFacade() {
    BoardFileService boardFileService = new BoardFileService();
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

  public void startGame() {
  }

  public void restartGame() {
  }

  public void saveGame() {
  }

  public void addPlayer() {
  }

  public void playTurn() {
  }

  public void registerObserver(BoardGameObserver observer) {
    if (currentGame != null) {
      currentGame.registerObserver(observer);
    }
  }

}

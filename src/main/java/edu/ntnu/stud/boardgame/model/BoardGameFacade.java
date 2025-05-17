package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.exception.files.BoardFileException;
import edu.ntnu.stud.boardgame.factory.BoardGameFactory;
import edu.ntnu.stud.boardgame.model.enums.BoardGameType;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
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

  public void createGame(BoardGameType type, String fileName) throws BoardFileException {
    currentGame = factory.loadGameFromFile(type, fileName);
  }

  public void startGame() {
  }

  public void restartGame() {
  }

  public void playTurn() {
  }
}

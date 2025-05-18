package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.exception.BoardGameException;
import edu.ntnu.stud.boardgame.model.BoardGameFacade;
import edu.ntnu.stud.boardgame.model.game.BoardGame;

public class GameController {

  private final MainController mainController;
  private final BoardGameFacade gameFacade;

  public GameController(MainController mainController, BoardGameFacade gameFacade) {
    this.mainController = mainController;
    this.gameFacade = gameFacade;
  }

  public void playTurn() throws BoardGameException {
    gameFacade.playTurn();
  }

  public void restartGame() throws BoardGameException {
    gameFacade.restartGame();
  }

  public BoardGame getGame() {
    return gameFacade.getCurrentGame();
  }

  public void showError(String title, String message) {
    mainController.showErrorDialog(title, message);
  }

  public void showInfo(String title, String message) {
    mainController.showInfoDialog(title, message);
  }

  public void goToGameSelection() {
    mainController.showGameSelectionView();
  }
}
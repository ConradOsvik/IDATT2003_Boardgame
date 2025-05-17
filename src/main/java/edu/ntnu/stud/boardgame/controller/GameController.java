package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.BoardGameFacade;

public class GameController {

  private final MainController mainController;
  private final BoardGameFacade gameFacade;

  public GameController(MainController controller, BoardGameFacade gameFacade) {
    this.mainController = controller;
    this.gameFacade = gameFacade;
  }
}

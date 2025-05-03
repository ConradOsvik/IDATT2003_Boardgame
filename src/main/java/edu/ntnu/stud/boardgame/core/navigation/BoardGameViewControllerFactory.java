package edu.ntnu.stud.boardgame.core.navigation;

import edu.ntnu.stud.boardgame.common.controller.MainMenuController;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameSelectionController;

public class BoardGameViewControllerFactory implements ViewControllerFactory {

  public ViewController createViewController(ViewName viewName) {
    return switch (viewName) {
      case MAIN_MENU -> new MainMenuController();
      case SL_GAME_SELECTION -> new SlGameSelectionController();
      case SL_GAME -> new SlGameController();
    };
  }
}

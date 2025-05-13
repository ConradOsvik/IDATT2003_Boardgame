package edu.ntnu.stud.boardgame.core.navigation;

import edu.ntnu.stud.boardgame.common.controller.MainMenuController;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameSelectionController;

public class BoardGameViewControllerFactory implements ViewControllerFactory {

  private final Navigator navigator;
  private final ErrorDialog errorDialog;

  public BoardGameViewControllerFactory(Navigator navigator, ErrorDialog errorDialog) {
    this.navigator = navigator;
    this.errorDialog = errorDialog;
  }

  public ViewController createViewController(ViewName viewName) {
    return switch (viewName) {
      case MAIN_MENU -> new MainMenuController(navigator, errorDialog);
      case SL_GAME_SELECTION -> new SlGameSelectionController(navigator, errorDialog);
      case SL_GAME -> new SlGameController(navigator, errorDialog);
    };
  }
}

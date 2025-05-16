package edu.ntnu.stud.boardgame.core.navigation;

public interface ViewControllerFactory {

  enum ViewName {
    MAIN_MENU,
    SL_GAME_SELECTION,
    SL_GAME,
  }

  ViewController createViewController(ViewName viewName);
}

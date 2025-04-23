package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.snakesandladders.controller.GameBoardController;

public class GameBoardView implements BoardGameObserver {
  private final GameBoardController controller;

  public GameBoardView(GameBoardController controller) {
    this.controller = controller;
  }

  public void onGameEvent(GameEvent event){}
}

package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.snakesandladders.controller.ControlPanelController;

public class ControlPanelView implements BoardGameObserver {
  private final ControlPanelController controller;

  public ControlPanelView(ControlPanelController controller) {
    this.controller = controller;
  }

  public void onGameEvent(GameEvent event){

  }
}

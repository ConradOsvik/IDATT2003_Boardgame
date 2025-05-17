package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import javafx.scene.layout.BorderPane;

public class LadderBoard extends BorderPane implements BoardGameObserver {

  private final MainController controller;
  private final GameController gameController;

  public LadderBoard(MainController controller, GameController gameController) {
    this.controller = controller;
    this.gameController = gameController;
    
    controller.registerObserver(this);
  }

  @Override
  public void onGameEvent(GameEvent event) {

  }
}

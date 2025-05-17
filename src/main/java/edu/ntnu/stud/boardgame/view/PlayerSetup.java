package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import javafx.scene.layout.BorderPane;

public class PlayerSetup extends BorderPane implements BoardGameObserver {

  public PlayerSetup(MainController controller) {
    controller.registerObserver(this);
  }

  @Override
  public void onGameEvent(GameEvent event) {

  }
}

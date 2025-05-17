package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import javafx.scene.layout.BorderPane;

public class GameSelection extends BorderPane implements BoardGameObserver {

  public GameSelection(MainController controller) {
    controller.registerObserver(this);
  }

  @Override
  public void onGameEvent(GameEvent event) {

  }
}

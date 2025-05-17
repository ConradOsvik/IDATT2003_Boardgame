package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import javafx.scene.layout.BorderPane;

public class BoardSelection extends BorderPane implements BoardGameObserver {

  public BoardSelection(MainController controller) {
    controller.registerObserver(this);
  }

  @Override
  public void onGameEvent(GameEvent event) {

  }
}

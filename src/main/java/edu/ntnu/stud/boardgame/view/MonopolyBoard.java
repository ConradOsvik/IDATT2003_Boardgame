package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import javafx.scene.layout.BorderPane;

public class MonopolyBoard extends BorderPane implements BoardGameObserver {

  public MonopolyBoard(MainController controller, GameController gameController) {
    gameController.registerObserver(this);
  }

  @Override
  public void onGameEvent(GameEvent event) {

  }
}

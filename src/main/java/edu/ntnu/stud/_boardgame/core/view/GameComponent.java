package edu.ntnu.stud.boardgame.core.view;

import edu.ntnu.stud.boardgame.core.controller.GameController;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import java.util.logging.Logger;
import javafx.scene.Node;

public abstract class GameComponent<T extends Node> implements BoardGameObserver {

  protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
  protected final T node;
  protected GameController controller;
  protected ErrorDialog errorDialog;

  protected GameComponent(GameController controller, T node, ErrorDialog errorDialog) {
    this.controller = controller;
    this.node = node;
    this.errorDialog = errorDialog;
    controller.addObserver(this);
  }

  public void init() {
  }

  public T getNode() {
    return node;
  }
}

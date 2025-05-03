package edu.ntnu.stud.boardgame.core.navigation;

import javafx.scene.Node;

public interface ViewController {

  void init(Object... args);

  Node getView();
}

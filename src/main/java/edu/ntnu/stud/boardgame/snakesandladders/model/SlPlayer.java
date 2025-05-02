package edu.ntnu.stud.boardgame.snakesandladders.model;

import edu.ntnu.stud.boardgame.core.model.Player;
import javafx.scene.paint.Color;

public class SlPlayer extends Player {

  private final Color color;

  public SlPlayer(String name, Color color) {
    super(name);
    requireNotNull(color, "Color cannot be null");
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}

package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

public class PropertyAction implements TileAction {

  private final int price;
  private Player owner;

  public PropertyAction(int price) {
    if (price < 0) {
      throw new IllegalArgumentException("Property price cannot be negative.");
    }
    this.price = price;
    this.owner = null;
  }

  @Override
  public void perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for PropertyAction.");
    }
    MonopolyActionRegistry.getInstance().executePropertyAction(player, this);
  }

  public Player getOwner() {
    return owner;
  }

  public void setOwner(Player owner) {
    this.owner = owner;
  }

  public int getPrice() {
    return price;
  }
}
package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

public class PropertyAction implements TileAction {

  private final int price;
  private Player owner;

  public PropertyAction(int price) {
    this.price = price;
    this.owner = null;
  }

  @Override
  public void perform(Player player) {
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
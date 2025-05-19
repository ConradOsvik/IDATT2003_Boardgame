package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;

public class PropertyAction implements TileAction {

  private final int price;
  private final MonopolyGame game;
  private Player owner;

  public PropertyAction(int price, MonopolyGame game) {
    this.price = price;
    this.game = game;
    this.owner = null;
  }

  @Override
  public void perform(Player player) {
    if (owner != null && owner != player) {
      int rent = price / 5;
      game.payRent(player, owner, rent);
    }
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
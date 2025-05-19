package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

public class TaxAction implements TileAction {

  private final int amount;

  public TaxAction(int amount) {
    this.amount = amount;
  }

  @Override
  public void perform(Player player) {
    MonopolyActionRegistry.getInstance().executeTaxAction(player, amount);
  }

  public int getAmount() {
    return amount;
  }
}
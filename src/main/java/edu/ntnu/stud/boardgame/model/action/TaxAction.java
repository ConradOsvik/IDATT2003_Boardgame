package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

public class TaxAction implements TileAction {

  private final int amount;

  public TaxAction(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Tax amount cannot be negative.");
    }
    this.amount = amount;
  }

  @Override
  public void perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for TaxAction.");
    }
    MonopolyActionRegistry.getInstance().executeTaxAction(player, amount);
  }

  public int getAmount() {
    return amount;
  }
}
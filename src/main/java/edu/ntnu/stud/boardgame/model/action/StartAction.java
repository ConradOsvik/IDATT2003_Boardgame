package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

public class StartAction implements TileAction {

  private final int amount;

  public StartAction(int amount) {
    this.amount = amount;
  }

  @Override
  public void perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for StartAction.");
    }
    MonopolyActionRegistry.getInstance().executeStartAction(player, amount);
  }

  public int getAmount() {
    return amount;
  }
}
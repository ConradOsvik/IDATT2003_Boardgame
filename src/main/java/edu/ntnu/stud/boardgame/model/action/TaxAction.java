package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;

public class TaxAction implements TileAction {

  private final int amount;
  private final MonopolyGame game;

  public TaxAction(int amount, MonopolyGame game) {
    this.amount = amount;
    this.game = game;
  }

  @Override
  public void perform(Player player) {
    game.payTax(player, amount);
  }

  public int getAmount() {
    return amount;
  }
}
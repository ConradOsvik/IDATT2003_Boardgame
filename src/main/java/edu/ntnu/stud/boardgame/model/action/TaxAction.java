package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

/**
 * Represents a tax tile action in Monopoly.
 *
 * <p>Charges the player a fixed tax amount when landing on the tile.
 */
public class TaxAction implements TileAction {

  private final int amount;

  /**
   * Creates a tax action with the specified amount.
   *
   * @param amount the tax amount to charge
   * @throws IllegalArgumentException if amount is negative
   */
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

  /**
   * Gets the tax amount.
   *
   * @return the tax amount
   */
  public int getAmount() {
    return amount;
  }
}

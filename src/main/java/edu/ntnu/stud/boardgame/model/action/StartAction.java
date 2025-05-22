package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

/**
 * Represents the start tile action in Monopoly.
 * 
 * <p>
 * Awards the player a bonus amount when landing on or passing the start tile.
 * </p>
 */
public class StartAction implements TileAction {

  private final int amount;

  /**
   * Creates a start action with the specified bonus amount.
   *
   * @param amount the bonus amount to award
   */
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

  /**
   * Gets the bonus amount.
   *
   * @return the bonus amount
   */
  public int getAmount() {
    return amount;
  }
}
package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;

/**
 * Represents a property tile action in Monopoly.
 * 
 * <p>
 * Handles property ownership and rent payments between players.
 * </p>
 */
public class PropertyAction implements TileAction {

  private final int price;
  private Player owner;

  /**
   * Creates a property action with the specified price.
   *
   * @param price the property's price
   * @throws IllegalArgumentException if price is negative
   */
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

  /**
   * Gets the current owner of this property.
   *
   * @return the owner, or null if unowned
   */
  public Player getOwner() {
    return owner;
  }

  /**
   * Sets the owner of this property.
   *
   * @param owner the new owner
   */
  public void setOwner(Player owner) {
    this.owner = owner;
  }

  /**
   * Gets the property's price.
   *
   * @return the price
   */
  public int getPrice() {
    return price;
  }
}
package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when a player purchases a property in Monopoly.
 * 
 * <p>
 * Contains details about the purchase including the player,
 * property tile, and purchase price.
 * </p>
 */
public class PropertyPurchasedEvent extends GameEvent {

  private final Player player;
  private final Tile property;
  private final int price;

  /**
   * Creates a new property purchased event.
   *
   * @param player   the player making the purchase
   * @param property the property being purchased
   * @param price    the purchase price
   * @throws IllegalArgumentException if player or property is null
   */
  public PropertyPurchasedEvent(Player player, Tile property, int price) {
    super(EventType.PROPERTY_PURCHASED);
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for PropertyPurchasedEvent.");
    }
    if (property == null) {
      throw new IllegalArgumentException("Property cannot be null for PropertyPurchasedEvent.");
    }
    this.player = player;
    this.property = property;
    this.price = price;
  }

  /**
   * Gets the purchasing player.
   *
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Gets the purchased property tile.
   *
   * @return the property
   */
  public Tile getProperty() {
    return property;
  }

  /**
   * Gets the purchase price.
   *
   * @return the price
   */
  public int getPrice() {
    return price;
  }
}
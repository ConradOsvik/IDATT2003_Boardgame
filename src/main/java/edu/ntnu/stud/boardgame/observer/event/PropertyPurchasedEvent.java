package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class PropertyPurchasedEvent extends GameEvent {

  private final Player player;
  private final Tile property;
  private final int price;

  public PropertyPurchasedEvent(Player player, Tile property, int price) {
    super(EventType.PLAYER_MOVED);
    this.player = player;
    this.property = property;
    this.price = price;
  }

  public Player getPlayer() {
    return player;
  }

  public Tile getProperty() {
    return property;
  }

  public int getPrice() {
    return price;
  }
}
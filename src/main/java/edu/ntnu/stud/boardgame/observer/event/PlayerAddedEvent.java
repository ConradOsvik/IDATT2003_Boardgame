package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class PlayerAddedEvent extends GameEvent {

  private final Player player;

  public PlayerAddedEvent(Player player) {
    super(EventType.PLAYER_ADDED);
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for PlayerAddedEvent.");
    }
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}

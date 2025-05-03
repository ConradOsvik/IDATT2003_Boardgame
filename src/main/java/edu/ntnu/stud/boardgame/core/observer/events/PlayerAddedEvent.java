package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;

public class PlayerAddedEvent extends GameEvent {

  protected final Player player;

  public PlayerAddedEvent(Player player) {
    super(EventType.PLAYER_ADDED);
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}

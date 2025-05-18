package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class PlayerAddedEvent extends GameEvent {

  private Player player;

  public PlayerAddedEvent(Player player) {
    super(EventType.PLAYER_ADDED);
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}

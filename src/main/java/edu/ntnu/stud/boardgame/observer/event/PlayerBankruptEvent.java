package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class PlayerBankruptEvent extends GameEvent {

  private final Player player;

  public PlayerBankruptEvent(Player player) {
    super(EventType.PLAYER_MOVED);
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
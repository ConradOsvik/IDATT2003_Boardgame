package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class PlayerBankruptEvent extends GameEvent {

  private final Player player;

  public PlayerBankruptEvent(Player player) {
    super(EventType.PLAYER_BANKRUPT);
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null for PlayerBankruptEvent.");
    }
    this.player = player;
  }

  public Player getPlayer() {
    return player;
  }
}
package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class MoneyTransferEvent extends GameEvent {

  private final Player fromPlayer;
  private final Player toPlayer;
  private final int amount;
  private final String reason;

  public MoneyTransferEvent(Player fromPlayer, Player toPlayer, int amount, String reason) {
    super(EventType.PLAYER_MOVED);
    this.fromPlayer = fromPlayer;
    this.toPlayer = toPlayer;
    this.amount = amount;
    this.reason = reason;
  }

  public Player getFromPlayer() {
    return fromPlayer;
  }

  public Player getToPlayer() {
    return toPlayer;
  }

  public int getAmount() {
    return amount;
  }

  public String getReason() {
    return reason;
  }
}
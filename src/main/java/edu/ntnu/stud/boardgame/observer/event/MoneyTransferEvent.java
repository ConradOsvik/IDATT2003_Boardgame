package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

public class MoneyTransferEvent extends GameEvent {

  private final Player fromPlayer; // Can be null (e.g., for bank transactions)
  private final Player toPlayer; // Can be null (e.g., for bank transactions)
  private final int amount;
  private final String reason;

  public MoneyTransferEvent(Player fromPlayer, Player toPlayer, int amount, String reason) {
    super(EventType.MONEY_TRANSFER);
    if (reason == null || reason.trim().isEmpty()) {
      throw new IllegalArgumentException("Reason for money transfer cannot be null or empty.");
    }
    // fromPlayer and toPlayer can be null as per design
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
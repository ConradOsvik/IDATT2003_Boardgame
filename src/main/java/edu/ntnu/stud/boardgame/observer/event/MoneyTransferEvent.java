package edu.ntnu.stud.boardgame.observer.event;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.GameEvent;

/**
 * Event fired when money is transferred between players in Monopoly.
 * 
 * <p>
 * Contains details about the transfer including source and destination
 * players, amount, and reason for the transfer.
 * </p>
 */
public class MoneyTransferEvent extends GameEvent {

  private final Player fromPlayer;
  private final Player toPlayer;
  private final int amount;
  private final String reason;

  /**
   * Creates a new money transfer event.
   *
   * @param fromPlayer the player sending money (null for bank)
   * @param toPlayer   the player receiving money (null for bank)
   * @param amount     the amount of money transferred
   * @param reason     description of why the transfer occurred
   * @throws IllegalArgumentException if reason is null or empty
   */
  public MoneyTransferEvent(Player fromPlayer, Player toPlayer, int amount, String reason) {
    super(EventType.MONEY_TRANSFER);
    if (reason == null || reason.trim().isEmpty()) {
      throw new IllegalArgumentException("Reason for money transfer cannot be null or empty.");
    }

    this.fromPlayer = fromPlayer;
    this.toPlayer = toPlayer;
    this.amount = amount;
    this.reason = reason;
  }

  /**
   * Gets the player sending money.
   *
   * @return the source player, or null if bank
   */
  public Player getFromPlayer() {
    return fromPlayer;
  }

  /**
   * Gets the player receiving money.
   *
   * @return the destination player, or null if bank
   */
  public Player getToPlayer() {
    return toPlayer;
  }

  /**
   * Gets the transfer amount.
   *
   * @return the amount
   */
  public int getAmount() {
    return amount;
  }

  /**
   * Gets the reason for the transfer.
   *
   * @return the reason
   */
  public String getReason() {
    return reason;
  }
}
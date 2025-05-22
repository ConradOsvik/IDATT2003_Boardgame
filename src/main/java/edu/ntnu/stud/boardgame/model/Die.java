package edu.ntnu.stud.boardgame.model;

/**
 * Represents a single six-sided die.
 * 
 * <p>
 * Simulates rolling a die and tracks its last rolled value.
 * </p>
 */
public class Die {

  private int lastRolledValue = 0;

  /**
   * Rolls the die and returns a random value between 1 and 6.
   *
   * @return the rolled value
   */
  public int roll() {
    lastRolledValue = (int) (Math.random() * 6) + 1;
    return lastRolledValue;
  }

  /**
   * Gets the last rolled value.
   *
   * @return the last value rolled, or 0 if never rolled
   */
  public int getValue() {
    return lastRolledValue;
  }
}

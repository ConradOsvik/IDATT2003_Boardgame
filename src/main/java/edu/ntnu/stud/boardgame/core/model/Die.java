package edu.ntnu.stud.boardgame.core.model;

/**
 * Represents a single six-sided die used in the board game. This class provides methods to roll the
 * die and retrieve its current value.
 */
class Die {

  /**
   * The last rolled value of this die, initialized to 0 before first roll.
   */
  private int lastRolledValue;

  /**
   * Constructs a new die with an initial value of 0.
   */
  Die() {
    this.lastRolledValue = 0;
  }

  /**
   * Rolls the die and returns a random value between 1 and 6. The result is also stored as the last
   * rolled value.
   *
   * @return A random integer between 1 and 6
   */
  int roll() {
    this.lastRolledValue = (int) (Math.random() * 6) + 1;
    return this.lastRolledValue;
  }

  /**
   * Gets the last rolled value of this die. Returns 0 if the die has not been rolled yet.
   *
   * @return The last rolled value, or 0 if the die has not been rolled
   */
  int getValue() {
    return this.lastRolledValue;
  }

  @Override
  public String toString() {
    return "Die{value=" + lastRolledValue + "}";
  }
}

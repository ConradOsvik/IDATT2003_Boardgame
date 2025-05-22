package edu.ntnu.stud.boardgame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of dice for rolling in board games.
 *
 * <p>Manages multiple dice and provides methods for rolling them.
 */
public class Dice {

  private final List<Die> dice;

  /**
   * Creates a collection of dice.
   *
   * @param numberOfDice number of dice to create
   * @throws IllegalArgumentException if numberOfDice is not positive
   */
  public Dice(int numberOfDice) {
    if (numberOfDice <= 0) {
      throw new IllegalArgumentException("Number of dice must be positive.");
    }
    this.dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      this.dice.add(new Die());
    }
  }

  /**
   * Rolls all dice and returns the total.
   *
   * @return sum of all dice values
   */
  public int roll() {
    int total = 0;
    for (Die die : dice) {
      total += die.roll();
    }
    return total;
  }

  /**
   * Gets the value of a specific die.
   *
   * @param dieNumber index of the die
   * @return current value of the die
   * @throws IndexOutOfBoundsException if dieNumber is invalid
   */
  public int getDie(int dieNumber) {
    if (dieNumber < 0 || dieNumber >= dice.size()) {
      throw new IndexOutOfBoundsException("Die number out of bounds");
    }
    return dice.get(dieNumber).getValue();
  }
}

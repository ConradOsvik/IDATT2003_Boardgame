package edu.ntnu.stud.boardgame.core.model;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents a collection of dice used in the board game. This class manages multiple die instances
 * and provides methods to roll them all at once.
 */
public class Dice {

  /**
   * The collection of individual dice.
   */
  private final List<Die> dice;

  /**
   * Constructs a new collection of dice with the specified number of die instances.
   *
   * @param numberOfDice The number of dice to create
   * @throws IllegalArgumentException if the number of dice is less than 1
   */
  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("Number of dice must be at least 1");
    }

    dice = IntStream.range(0, numberOfDice)
        .mapToObj(i -> new Die())
        .toList();
  }

  /**
   * Rolls all dice and returns the sum of their values.
   *
   * @return The total sum of all dice values
   */
  public int roll() {
    return dice.stream().mapToInt(Die::roll).sum();
  }

  /**
   * Gets the value of a specific die in the collection.
   *
   * @param dieNumber The index of the die (0-based)
   * @return The current value of the specified die
   * @throws IndexOutOfBoundsException if the die number is out of range
   */
  int getDie(int dieNumber) {
    return dice.get(dieNumber).getValue();
  }
}

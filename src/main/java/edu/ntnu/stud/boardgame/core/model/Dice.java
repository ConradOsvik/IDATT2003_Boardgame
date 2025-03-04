package edu.ntnu.stud.boardgame.core.model;

import java.util.List;
import java.util.stream.IntStream;

class Dice {

  private final List<Die> dice;

  Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("Number of dice must be at least 1");
    }

    dice = IntStream.range(0, numberOfDice)
        .mapToObj(i -> new Die())
        .toList();
  }

  int roll() {
    return dice.stream().mapToInt(Die::roll).sum();
  }

  int getDie(int dieNumber) {
    return dice.get(dieNumber).getValue();
  }
}

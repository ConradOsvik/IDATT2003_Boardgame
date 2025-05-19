package edu.ntnu.stud.boardgame.model;

public class Die {

  private int lastRolledValue = 0;

  public int roll() {
    lastRolledValue = (int) (Math.random() * 6) + 1;
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }
}

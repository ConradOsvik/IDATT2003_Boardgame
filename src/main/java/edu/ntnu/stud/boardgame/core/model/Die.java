package edu.ntnu.stud.boardgame.core.model;

class Die {

  private int lastRolledValue;

  Die() {
    this.lastRolledValue = 0;
  }

  int roll() {
    this.lastRolledValue = (int) (Math.random() * 6) + 1;
    return this.lastRolledValue;
  }

  int getValue() {
    return this.lastRolledValue;
  }
}

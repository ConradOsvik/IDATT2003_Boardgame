package edu.ntnu.stud.boardgame.model;

class Die {
    private int lastRolledValue;

    public int roll() {
        lastRolledValue = (int) (Math.random() * 6) + 1;
        return lastRolledValue;
    }

    public int getValue() {
        return lastRolledValue;
    }
}
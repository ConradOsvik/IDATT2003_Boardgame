package edu.ntnu.stud.boardgame.model;

import java.util.ArrayList;
import java.util.List;

class Dice {
    private List<Die> dice;

    public Dice(int numberOfDice) {
        dice = new ArrayList<>();
        for (int i = 0; i < numberOfDice; i++) {
            dice.add(new Die());
        }
    }

    public int roll() {
        return dice.stream().mapToInt(Die::roll).sum();
    }

    public int getDie(int index) {
        return dice.get(index).getValue();
    }
}
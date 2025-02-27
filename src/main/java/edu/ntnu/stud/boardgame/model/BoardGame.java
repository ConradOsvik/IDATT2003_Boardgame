package edu.ntnu.stud.boardgame.model;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {
    private Board board;
    private Player currentPlayer;
    List<Player> players = new ArrayList<>();
    private Dice dice;

    public void addPlayer(Player player) {
        if(player == null) {
            throw new NullPointerException("Player cannot be null");
        }

        players.add(player);
    }

    public void createBoard() {
        board = new Board();
    }

    public void createDice() {
        dice = new Dice(1);
    }

    public void play() {
        for (Player player : players) {
            currentPlayer = player;
            int steps = dice.roll();
            player.move(steps);
        }
    }

    public Player getWinner() {
        for (Player player : players) {
            if (player.getCurrentTile().getTileId() == 90) {
                return player;
            }
        }
        return null;
    }

    public Board getBoard() {
        return board;
    }
}
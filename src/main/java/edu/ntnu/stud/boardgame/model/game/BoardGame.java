package edu.ntnu.stud.boardgame.model.game;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Dice;
import edu.ntnu.stud.boardgame.model.Player;
import java.util.ArrayList;
import java.util.List;

public abstract class BoardGame {

  protected final List<Player> players;
  protected Board board;
  protected int currentPlayerIndex = 0;
  protected Player currentPlayer;
  protected Dice dice;

  public BoardGame() {
    this.players = new ArrayList<>();
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void resetGame() {
    players.clear();
    currentPlayerIndex = 0;
    currentPlayer = null;
    board = null;
  }
}

package edu.ntnu.stud.boardgame.core.model;

import java.util.ArrayList;
import java.util.List;

public class BoardGame extends BaseModel {

  private Board board;
  private Player currentPlayer;
  private List<Player> players = new ArrayList<>();
  private Dice dice;
  private Player winner;
  private boolean finished = false;

  public void addPlayer(Player player) {
    requireNotNull(player, "Player cannot be null");
    players.add(player);
    Tile startTile = board.getTile(1);
    player.placeOnTile(startTile);
  }

  public void createBoard() {
    board = new Board();

    Tile previousTile = null;
    for (int i = 1; i <= 90; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);

      if (previousTile != null) {
        previousTile.addNextTile(tile);
      }

      previousTile = tile;
    }
  }

  public void createDice(int dices) {
    dice = new Dice(dices);
  }

  public void play() {
    for (Player player : players) {
      currentPlayer = player;

      int steps = dice.roll();

      currentPlayer.move(Tile.Direction.FORWARD, steps);

      if (currentPlayer.getCurrentTile().getTileId() == 90) {
        winner = currentPlayer;
        finished = true;
        break;
      }
    }
  }

  public Player getWinner() {
    return winner;
  }

  public boolean isFinished() {
    return finished;
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  Board getBoard() {
    return board;
  }
}

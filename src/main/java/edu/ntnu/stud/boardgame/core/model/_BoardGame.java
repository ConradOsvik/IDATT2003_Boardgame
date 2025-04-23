//package edu.ntnu.stud.boardgame.core.model;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Represents the board game with players, a board, and dice. This class manages the game state,
// * player turns, and win conditions.
// */
//public class _BoardGame extends BaseModel {
//
//  /**
//   * The game board containing tiles.
//   */
//  private _Board board;
//
//  /**
//   * The player whose turn it currently is.
//   */
//  private Player currentPlayer;
//
//  /**
//   * List of all players in the game.
//   */
//  private List<Player> players = new ArrayList<>();
//
//  /**
//   * The dice used for determining movement in the game.
//   */
//  private Dice dice;
//
//  /**
//   * The player who has won the game, or null if no winner yet.
//   */
//  private Player winner;
//
//  /**
//   * Flag indicating whether the game has finished.
//   */
//  private boolean finished = false;
//
//  /**
//   * Adds a player to the game and places them on the starting tile (tile ID 1).
//   *
//   * @param player The player to add to the game
//   * @throws IllegalArgumentException if the player is null
//   */
//  public void addPlayer(Player player) {
//    requireNotNull(player, "Player cannot be null");
//    players.add(player);
//    Tile startTile = board.getTile(1);
//    player.placeOnTile(startTile);
//  }
//
//  /**
//   * Creates the game board with 90 sequentially connected tiles. This method must be called before
//   * players can be added to the game.
//   */
//  public void createBoard() {
//    board = new _Board();
//
//    Tile previousTile = null;
//    for (int i = 1; i <= 90; i++) {
//      Tile tile = new Tile(i);
//      board.addTile(tile);
//
//      if (previousTile != null) {
//        previousTile.addNextTile(tile);
//      }
//
//      previousTile = tile;
//    }
//  }
//
//  /**
//   * Creates an empty board without any connections between tiles.
//   * This is used when loading a board configuration from a file.
//   */
//  public void createEmptyBoard() {
//    board = new _Board();
//  }
//
//  /**
//   * Creates dice for the game with the specified number of dice.
//   *
//   * @param dices The number of dice to use in the game
//   * @throws IllegalArgumentException if the number of dice is less than 1
//   */
//  public void createDice(int dices) {
//    dice = new Dice(dices);
//  }
//
//  /**
//   * Plays one round of the game, letting each player take a turn. For each player, rolls the dice
//   * and moves the player accordingly. If a player reaches tile 90, they win and the game is
//   * finished.
//   */
//  public void play() {
//    for (Player player : players) {
//      currentPlayer = player;
//
//      int steps = dice.roll();
//
//      currentPlayer.move(Tile.Direction.FORWARD, steps);
//
//      if (currentPlayer.getCurrentTile().getTileId() == 90) {
//        winner = currentPlayer;
//        finished = true;
//        break;
//      }
//    }
//  }
//
//  /**
//   * Gets the winner of the game.
//   *
//   * @return The player who has won the game, or null if there is no winner yet
//   */
//  public Player getWinner() {
//    return winner;
//  }
//
//  /**
//   * Checks if the game has finished.
//   *
//   * @return true if the game has finished, false otherwise
//   */
//  public boolean isFinished() {
//    return finished;
//  }
//
//  /**
//   * Gets a defensive copy of the list of players in the game.
//   *
//   * @return A new list containing all players in the game
//   */
//  public List<Player> getPlayers() {
//    return new ArrayList<>(players);
//  }
//
//  /**
//   * Gets the game board.
//   *
//   * @return The game board
//   */
//  public _Board getBoard() {
//    return board;
//  }
//}

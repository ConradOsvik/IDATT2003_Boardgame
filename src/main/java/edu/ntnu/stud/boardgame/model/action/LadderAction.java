package edu.ntnu.stud.boardgame.model.action;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;

/**
 * Represents a ladder tile action in Snakes and Ladders.
 * 
 * <p>
 * Moves the player up to a specified destination tile.
 * </p>
 */
public class LadderAction implements TileAction {

  private final Tile destinationTile;

  /**
   * Creates a ladder action with the specified destination tile.
   *
   * @param destinationTile the tile to move the player to
   * @throws IllegalArgumentException if destinationTile is null
   */
  public LadderAction(Tile destinationTile) {
    if (destinationTile == null) {
      throw new IllegalArgumentException("Destination tile cannot be null");
    }

    this.destinationTile = destinationTile;
  }

  @Override
  public void perform(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null");
    }

    player.placeOnTile(destinationTile);
  }

  /**
   * Gets the destination tile.
   *
   * @return the destination tile
   */
  public Tile getDestinationTile() {
    return destinationTile;
  }
}

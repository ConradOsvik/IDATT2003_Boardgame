package edu.ntnu.stud.boardgame.view.components.piece;

import edu.ntnu.stud.boardgame.model.Player;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * A visual representation of a player's game piece. Loads and displays the player's token image,
 * with fallback to a default image. Extends {@link StackPane} to center the piece image and handle
 * resizing.
 *
 * @see Player
 * @see ImageView
 */
public class PlayerPiece extends StackPane {

  private static final Logger LOGGER = Logger.getLogger(PlayerPiece.class.getName());

  private final Player player;
  private final ImageView tokenImageView;
  private double currentSize = 30;

  /**
   * Creates a new player piece with the specified player's token image.
   *
   * <p>If the player's token image cannot be found, falls back to a default image.
   *
   * @param player The player this piece represents
   */
  public PlayerPiece(Player player) {
    this.player = player;
    this.tokenImageView = new ImageView();

    try {
      String imagePath = "/images/pieces/" + player.getPiece().getDisplayName() + ".png";
      URL resource = getClass().getResource(imagePath);

      if (resource != null) {
        tokenImageView.setImage(new Image(resource.toExternalForm()));
      } else {
        LOGGER.log(Level.WARNING, "Could not find image at path: " + imagePath);
        URL defaultResource = getClass().getResource("/images/pieces/default.png");
        if (defaultResource != null) {
          tokenImageView.setImage(new Image(defaultResource.toExternalForm()));
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error loading token image", e);
    }

    this.setAlignment(Pos.CENTER);

    updateSize(currentSize);
    getChildren().add(tokenImageView);

    setPickOnBounds(false);
    tokenImageView.setPickOnBounds(false);
  }

  /**
   * Updates the size of the player piece.
   *
   * <p>Maintains the aspect ratio of the token image while resizing.
   *
   * @param size The new size in pixels for both width and height
   */
  public void updateSize(double size) {
    this.currentSize = size;
    tokenImageView.setFitWidth(size);
    tokenImageView.setFitHeight(size);
    tokenImageView.setPreserveRatio(true);
  }

  /**
   * Gets the current size of the player piece.
   *
   * @return The current size in pixels
   */
  public double getCurrentSize() {
    return currentSize;
  }
}

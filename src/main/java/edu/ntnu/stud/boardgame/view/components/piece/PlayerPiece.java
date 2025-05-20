package edu.ntnu.stud.boardgame.view.components.piece;

import edu.ntnu.stud.boardgame.model.Player;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class PlayerPiece extends StackPane {

  private static final Logger LOGGER = Logger.getLogger(PlayerPiece.class.getName());

  private final Player player;
  private final ImageView tokenImageView;
  private double currentSize = 30;

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

  public void updateSize(double size) {
    this.currentSize = size;
    tokenImageView.setFitWidth(size);
    tokenImageView.setFitHeight(size);
    tokenImageView.setPreserveRatio(true);
  }

  public double getCurrentSize() {
    return currentSize;
  }
}
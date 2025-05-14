package edu.ntnu.stud.boardgame.snakesandladders.view.components.token;

import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Token extends Group {

  private static final Logger LOGGER = Logger.getLogger(Token.class.getName());
  private final ImageView tokenImageView;
  private final int tokenId;
  private double currentSize;

  public Token(int tokenId) {
    this.tokenId = tokenId;
    this.tokenImageView = new ImageView();
    getChildren().add(tokenImageView);

    // Set a default size
    updateSize(30);
  }

  public void updateSize(double newSize) {
    if (this.currentSize == newSize) {
      return;
    }

    this.currentSize = newSize;

    String tokenPath = "/pieces/snakesandladders/" + tokenId + ".png";
    try {
      URL resource = getClass().getResource(tokenPath);
      if (resource == null) {
        LOGGER.log(Level.SEVERE, "Token image not found: " + tokenPath);
        ErrorDialog.getInstance().showError("Resource Error",
            "Could not load token image: " + tokenPath);
        return;
      }

      Image image = new Image(resource.toExternalForm(), newSize, newSize, true, true);
      tokenImageView.setImage(image);

      // Center the image on the token position
      tokenImageView.setTranslateX(-newSize / 2);
      tokenImageView.setTranslateY(-newSize / 2);

    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Failed to load token image", e);
      ErrorDialog.getInstance().showError("Resource Error",
          "Failed to load token image: " + e.getMessage());
    }
  }
}
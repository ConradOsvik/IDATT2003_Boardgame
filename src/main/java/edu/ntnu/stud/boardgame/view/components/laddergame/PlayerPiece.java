package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class PlayerPiece extends StackPane {

  private static final Logger LOGGER = Logger.getLogger(PlayerPiece.class.getName());
  private static final Duration REGULAR_MOVE_DURATION = Duration.seconds(0.3);
  private static final Duration SPECIAL_MOVE_DURATION = Duration.seconds(0.5);

  private final Player player;
  private final ImageView tokenImageView;
  private final Queue<PathTransition> animationQueue = new LinkedList<>();
  private boolean isAnimating = false;

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

    tokenImageView.setFitWidth(30);
    tokenImageView.setFitHeight(30);
    tokenImageView.setPreserveRatio(true);

    tokenImageView.setTranslateX(-15);
    tokenImageView.setTranslateY(-15);

    getChildren().add(tokenImageView);
  }

  public void updateSize(double size) {
    tokenImageView.setFitWidth(size);
    tokenImageView.setFitHeight(size);
    tokenImageView.setTranslateX(-size / 2);
    tokenImageView.setTranslateY(-size / 2);
  }

  public void animateMove(Tile fromTile, Tile toTile, double cellSize, double padding) {
    Path path = createDirectPath(fromTile, toTile, cellSize, padding);
    queueAnimation(path, REGULAR_MOVE_DURATION);
  }

  public void animateLadderClimb(Tile fromTile, Tile toTile, double cellSize, double padding) {
    Path path = createLadderPath(fromTile, toTile, cellSize, padding);
    queueAnimation(path, SPECIAL_MOVE_DURATION);
  }

  public void animateSnakeSlide(Tile fromTile, Tile toTile, double cellSize, double padding) {
    Path path = createSnakePath(fromTile, toTile, cellSize, padding);
    queueAnimation(path, SPECIAL_MOVE_DURATION);
  }

  private Path createDirectPath(Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null || toTile == null || fromTile.getRow() == null
        || fromTile.getColumn() == null || toTile.getRow() == null || toTile.getColumn() == null) {
      return null;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));
    path.getElements().add(new LineTo(endX, endY));

    return path;
  }

  private Path createLadderPath(Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null || toTile == null || fromTile.getRow() == null
        || fromTile.getColumn() == null || toTile.getRow() == null || toTile.getColumn() == null) {
      return null;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));

    double midX = (startX + endX) / 2;
    double midY = (startY + endY) / 2;
    double controlX = midX + (startY < endY ? -cellSize : cellSize) / 2;
    double controlY = midY - cellSize / 2;

    path.getElements().add(new CubicCurveTo(midX, controlY, midX, controlY, endX, endY));

    return path;
  }

  private Path createSnakePath(Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null || toTile == null || fromTile.getRow() == null
        || fromTile.getColumn() == null || toTile.getRow() == null || toTile.getColumn() == null) {
      return null;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));

    double controlX1 = (startX + endX) / 2 + cellSize;
    double controlY1 = (startY + endY) / 2;
    double controlX2 = (startX + endX) / 2 - cellSize;
    double controlY2 = (startY + endY) / 2;

    path.getElements()
        .add(new CubicCurveTo(controlX1, controlY1, controlX2, controlY2, endX, endY));

    return path;
  }

  private void queueAnimation(Path path, Duration duration) {
    if (path == null) {
      return;
    }

    PathTransition transition = new PathTransition(duration, path, this);
    animationQueue.add(transition);

    if (!isAnimating) {
      playNextAnimation();
    }
  }

  private void playNextAnimation() {
    if (animationQueue.isEmpty()) {
      isAnimating = false;
      return;
    }

    isAnimating = true;
    PathTransition nextAnimation = animationQueue.poll();

    nextAnimation.setOnFinished(e -> playNextAnimation());
    nextAnimation.play();
  }
}
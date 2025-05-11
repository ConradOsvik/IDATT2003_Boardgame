package edu.ntnu.stud.boardgame.snakesandladders.view.components.token;

import edu.ntnu.stud.boardgame.core.exception.TokenImageNotFoundException;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.PathTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class PlayerToken extends Group {

  private static final Duration REGULAR_MOVE_DURATION = Duration.seconds(0.5);
  private static final Duration SPECIAL_MOVE_DURATION = Duration.seconds(0.3);

  private final Queue<PathTransition> animationQueue = new LinkedList<>();
  private boolean isAnimating = false;

  public PlayerToken(double size, int tokenId) {
    String basePath = "/pieces/snakesandladders/";
    String tokenPath = basePath + tokenId + ".png";

    URL resource = getClass().getResource(tokenPath);
    if (resource == null) {
      throw new TokenImageNotFoundException("Token image not found: " + tokenPath);
    }

    try {
      Image image = new Image(resource.toExternalForm(), size, size, true, true);
      ImageView tokenImage = new ImageView(image);

      tokenImage.setTranslateX(-size / 2);
      tokenImage.setTranslateY(-size / 2);

      getChildren().add(tokenImage);
    } catch (IllegalArgumentException e) {
      throw new TokenImageNotFoundException("Token image not found: " + tokenPath, e);
    }
  }

  public void positionAtTile(int tileId, SlBoard board, double cellSize, double padding) {
    if (tileId <= 0) {
      return;
    }

    double[] position = calculatePositionFromTileId(tileId, board, cellSize, padding);
    setTranslateX(position[0]);
    setTranslateY(position[1]);
  }

  public void animateRegularMove(int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    animateMove(PathType.REGULAR, fromTileId, toTileId, board, cellSize, padding);
  }

  public void animateSnakeMove(int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    animateMove(PathType.SNAKE, fromTileId, toTileId, board, cellSize, padding);
  }

  public void animateLadderMove(int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    animateMove(PathType.LADDER, fromTileId, toTileId, board, cellSize, padding);
  }

  public void animateBounceBackMove(int fromFinalTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    animateMove(PathType.BOUNCE_BACK, fromFinalTileId, toTileId, board, cellSize, padding);
  }

  private void animateMove(PathType pathType, int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    Path path = createPath(pathType, fromTileId, toTileId, board, cellSize, padding);
    Duration duration =
        (pathType == PathType.REGULAR) ? REGULAR_MOVE_DURATION : SPECIAL_MOVE_DURATION;
    queueAnimation(path, duration);
  }

  private void queueAnimation(Path path, Duration duration) {
    if (path == null) {
      return;
    }

    PathTransition newTransition = new PathTransition(duration, path, this);
    animationQueue.add(newTransition);

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

  private Path createPath(PathType type, int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    return switch (type) {
      case REGULAR -> createMovementPath(fromTileId, toTileId, board, cellSize, padding);
      case SNAKE -> createSnakePath(fromTileId, toTileId, board, cellSize, padding);
      case LADDER -> createLadderPath(fromTileId, toTileId, board, cellSize, padding);
      case BOUNCE_BACK -> createBounceBackPath(fromTileId, toTileId, board, cellSize, padding);
      default -> null;
    };
  }

  private double[] calculatePositionFromTileId(int tileId, SlBoard board, double cellSize,
      double padding) {
    int[] coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(
        tileId, board.getRows(), board.getColumns());
    double x = coords[1] * cellSize + padding + cellSize / 2;
    double y = coords[0] * cellSize + padding + cellSize / 2;
    return new double[]{x, y};
  }

  private Path createMovementPath(int fromTileId, int toTileId, SlBoard board, double cellSize,
      double padding) {
    Path path = new Path();
    double[] start = calculatePositionFromTileId(fromTileId, board, cellSize, padding);
    path.getElements().add(new MoveTo(start[0], start[1]));

    for (int currentTileId = fromTileId + 1; currentTileId <= toTileId; currentTileId++) {
      double[] position = calculatePositionFromTileId(currentTileId, board, cellSize, padding);
      path.getElements().add(new LineTo(position[0], position[1]));
    }

    return path;
  }

  private Path createSnakePath(int headTileId, int tailTileId, SlBoard board, double cellSize,
      double padding) {
    Path path = new Path();
    double[] head = calculatePositionFromTileId(headTileId, board, cellSize, padding);
    double[] tail = calculatePositionFromTileId(tailTileId, board, cellSize, padding);

    path.getElements().add(new MoveTo(head[0], head[1]));

    double controlX1 = (head[0] + tail[0]) / 2 + 30;
    double controlY1 = (head[1] + tail[1]) / 2 - 30;
    double controlX2 = (head[0] + tail[0]) / 2 - 30;
    double controlY2 = (head[1] + tail[1]) / 2 + 30;

    path.getElements().add(new CubicCurveTo(
        controlX1, controlY1,
        controlX2, controlY2,
        tail[0], tail[1]
    ));

    return path;
  }

  private Path createLadderPath(int bottomTileId, int topTileId, SlBoard board, double cellSize,
      double padding) {
    Path path = new Path();
    double[] bottom = calculatePositionFromTileId(bottomTileId, board, cellSize, padding);
    double[] top = calculatePositionFromTileId(topTileId, board, cellSize, padding);

    path.getElements().add(new MoveTo(bottom[0], bottom[1]));
    path.getElements().add(new LineTo(top[0], top[1]));

    return path;
  }

  private Path createBounceBackPath(int fromFinalTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    Path path = new Path();
    double[] start = calculatePositionFromTileId(fromFinalTileId, board, cellSize, padding);
    double[] end = calculatePositionFromTileId(toTileId, board, cellSize, padding);

    path.getElements().add(new MoveTo(start[0], start[1]));

    double controlX = (start[0] + end[0]) / 2;
    double controlY = Math.min(start[1], end[1]) - cellSize / 2;

    path.getElements().add(new CubicCurveTo(
        controlX, controlY,
        controlX, controlY,
        end[0], end[1]
    ));

    return path;
  }

  private enum PathType {
    REGULAR, SNAKE, LADDER, BOUNCE_BACK
  }
}
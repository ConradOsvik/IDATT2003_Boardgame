package edu.ntnu.stud.boardgame.view.components.piece;

import edu.ntnu.stud.boardgame.model.Tile;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javafx.animation.PathTransition;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

/**
 * Handles animations for game pieces on the board. Provides smooth movement animations for regular
 * moves, ladder climbs, snake slides, and bounce-back effects. Uses JavaFX path transitions for
 * fluid motion.
 *
 * @see PathTransition
 * @see PlayerPiece
 */
public class PieceAnimation {

  private static final Duration REGULAR_MOVE_DURATION = Duration.seconds(0.3);
  private static final Duration SPECIAL_MOVE_DURATION = Duration.seconds(0.5);
  private static final Duration STEP_DURATION = Duration.seconds(0.15);

  private final Map<PlayerPiece, Queue<PathTransition>> animationQueues = new HashMap<>();
  private final Map<PlayerPiece, Boolean> isAnimating = new HashMap<>();

  /**
   * Animates a piece moving from one tile to another in a regular movement.
   *
   * <p>For moves of more than one step, the piece will move tile by tile.
   *
   * @param piece The player piece to animate
   * @param fromTile The starting tile
   * @param toTile The destination tile
   * @param cellSize The size of each board cell
   * @param padding The board padding
   * @param steps Number of steps in the movement
   */
  public void animateRegularMove(
      PlayerPiece piece, Tile fromTile, Tile toTile, double cellSize, double padding, int steps) {
    if (fromTile == null || toTile == null) {
      return;
    }

    if (steps > 1) {
      animateStepByStep(piece, fromTile, toTile, cellSize, padding, steps);
    } else {
      Path path = createDirectPath(fromTile, toTile, cellSize, padding);
      queueAnimation(piece, path, STEP_DURATION);
    }
  }

  /**
   * Animates a piece climbing up a ladder.
   *
   * <p>Creates a curved upward animation path for smooth ladder movement.
   *
   * @param piece The player piece to animate
   * @param fromTile The bottom of the ladder
   * @param toTile The top of the ladder
   * @param cellSize The size of each board cell
   * @param padding The board padding
   */
  public void animateLadderClimb(
      PlayerPiece piece, Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null || toTile == null) {
      return;
    }
    Path path = createLadderPath(fromTile, toTile, cellSize, padding);
    queueAnimation(piece, path, SPECIAL_MOVE_DURATION);
  }

  /**
   * Animates a piece sliding down a snake.
   *
   * <p>Creates a curved downward animation path for smooth snake movement.
   *
   * @param piece The player piece to animate
   * @param fromTile The head of the snake
   * @param toTile The tail of the snake
   * @param cellSize The size of each board cell
   * @param padding The board padding
   */
  public void animateSnakeSlide(
      PlayerPiece piece, Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null || toTile == null) {
      return;
    }
    Path path = createSnakePath(fromTile, toTile, cellSize, padding);
    queueAnimation(piece, path, SPECIAL_MOVE_DURATION);
  }

  private void animateStepByStep(
      PlayerPiece piece, Tile fromTile, Tile toTile, double cellSize, double padding, int steps) {
    Tile currentTile = fromTile;
    Tile nextTile = currentTile.getNextTile();

    for (int i = 0; i < steps && nextTile != null; i++) {
      Path path = createDirectPath(currentTile, nextTile, cellSize, padding);
      queueAnimation(piece, path, STEP_DURATION);

      currentTile = nextTile;
      nextTile = currentTile.getNextTile();

      if (currentTile.equals(toTile)) {
        break;
      }
    }
  }

  /**
   * Animates a piece bouncing back to its previous position.
   *
   * <p>Used when a player cannot complete their move (e.g., overshooting the end).
   *
   * @param piece The player piece to animate
   * @param fromTile The tile to bounce from
   * @param toTile The tile to bounce back to
   * @param cellSize The size of each board cell
   * @param padding The board padding
   */
  public void animateBounceBack(
      PlayerPiece piece, Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null
        || toTile == null
        || fromTile.getRow() == null
        || fromTile.getColumn() == null
        || toTile.getRow() == null
        || toTile.getColumn() == null) {
      return;
    }

    double startX = fromTile.getColumn() * cellSize + padding + cellSize / 2;
    double startY = fromTile.getRow() * cellSize + padding + cellSize / 2;
    double endX = toTile.getColumn() * cellSize + padding + cellSize / 2;
    double endY = toTile.getRow() * cellSize + padding + cellSize / 2;

    Path path = new Path();
    path.getElements().add(new MoveTo(startX, startY));

    double midX = (startX + endX) / 2;
    double controlY = Math.min(startY, endY) - cellSize;

    path.getElements().add(new CubicCurveTo(midX, controlY, midX, controlY, endX, endY));

    queueAnimation(piece, path, SPECIAL_MOVE_DURATION);
  }

  private Path createDirectPath(Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null
        || toTile == null
        || fromTile.getRow() == null
        || fromTile.getColumn() == null
        || toTile.getRow() == null
        || toTile.getColumn() == null) {
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
    if (fromTile == null
        || toTile == null
        || fromTile.getRow() == null
        || fromTile.getColumn() == null
        || toTile.getRow() == null
        || toTile.getColumn() == null) {
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
    double controlY = midY - cellSize / 2;

    path.getElements().add(new CubicCurveTo(midX, controlY, midX, controlY, endX, endY));

    return path;
  }

  private Path createSnakePath(Tile fromTile, Tile toTile, double cellSize, double padding) {
    if (fromTile == null
        || toTile == null
        || fromTile.getRow() == null
        || fromTile.getColumn() == null
        || toTile.getRow() == null
        || toTile.getColumn() == null) {
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

  private void queueAnimation(PlayerPiece piece, Path path, Duration duration) {
    if (path == null) {
      return;
    }

    if (!animationQueues.containsKey(piece)) {
      animationQueues.put(piece, new LinkedList<>());
      isAnimating.put(piece, false);
    }

    Queue<PathTransition> queue = animationQueues.get(piece);

    PathTransition transition = new PathTransition(duration, path, piece);
    queue.add(transition);

    if (Boolean.FALSE.equals(isAnimating.get(piece))) {
      playNextAnimation(piece);
    }
  }

  private void playNextAnimation(PlayerPiece piece) {
    Queue<PathTransition> queue = animationQueues.get(piece);

    if (queue == null || queue.isEmpty()) {
      isAnimating.put(piece, false);
      return;
    }

    isAnimating.put(piece, true);
    PathTransition nextAnimation = queue.poll();

    if (nextAnimation == null) {
      isAnimating.put(piece, false);
      return;
    }

    nextAnimation.setOnFinished(
        e -> {
          if (animationQueues.containsKey(piece)) {
            playNextAnimation(piece);
          }
        });
    nextAnimation.play();
  }

  /**
   * Clears all pending animations for a specific piece.
   *
   * <p>Useful when resetting the game or when a piece needs to be moved immediately.
   *
   * @param piece The player piece whose animations should be cleared
   */
  public void clearAnimations(PlayerPiece piece) {
    if (animationQueues.containsKey(piece)) {
      animationQueues.get(piece).clear();
      isAnimating.put(piece, false);
    }
  }

  /**
   * Clears all pending animations for all pieces.
   *
   * <p>Useful when resetting the game or changing game state.
   */
  public void clearAllAnimations() {
    for (Queue<PathTransition> queue : animationQueues.values()) {
      queue.clear();
    }
    animationQueues.clear();
    isAnimating.clear();
  }
}

package edu.ntnu.stud.boardgame.snakesandladders.view.components.token;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
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

public class TokenAnimation {

  private static final Duration REGULAR_MOVE_DURATION = Duration.seconds(0.3);
  private static final Duration SPECIAL_MOVE_DURATION = Duration.seconds(0.5);
  private static final Duration STEP_DURATION = Duration.seconds(0.15);

  private final Map<Token, Queue<PathTransition>> animationQueues = new HashMap<>();
  private final Map<Token, Boolean> isAnimating = new HashMap<>();

  public void animateRegularMove(Token token, int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding, int steps) {
    // If the move is more than one step, animate through each intermediate step
    if (steps > 1) {
      animateStepByStep(token, fromTileId, toTileId, board, cellSize, padding, steps);
    } else {
      // Simple move from one tile to an adjacent tile
      Path path = createDirectPath(fromTileId, toTileId, board, cellSize, padding);
      queueAnimation(token, path, STEP_DURATION);
    }
  }

  private void animateStepByStep(Token token, int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding, int steps) {
    int currentTileId = fromTileId;

    for (int i = 0; i < steps; i++) {
      int nextTileId = currentTileId + 1;

      // If we've reached the destination, stop
      if (nextTileId > toTileId) {
        break;
      }

      // Create path for this step
      Path path = createDirectPath(currentTileId, nextTileId, board, cellSize, padding);
      queueAnimation(token, path, STEP_DURATION);

      currentTileId = nextTileId;
    }
  }

  public void animateSnakeSlide(Token token, int headTileId, int tailTileId, SlBoard board,
      double cellSize, double padding) {
    Path path = createCurvedPath(headTileId, tailTileId, board, cellSize, padding);
    queueAnimation(token, path, SPECIAL_MOVE_DURATION);
  }

  public void animateLadderClimb(Token token, int bottomTileId, int topTileId, SlBoard board,
      double cellSize, double padding) {
    Path path = createDirectPath(bottomTileId, topTileId, board, cellSize, padding);
    queueAnimation(token, path, SPECIAL_MOVE_DURATION);
  }

  public void animateBounceBack(Token token, int fromTileId, int toTileId, SlBoard board,
      double cellSize, double padding) {
    Path path = createArcPath(fromTileId, toTileId, board, cellSize, padding);
    queueAnimation(token, path, SPECIAL_MOVE_DURATION);
  }

  private Path createDirectPath(int fromTileId, int toTileId, SlBoard board, double cellSize,
      double padding) {
    double[] startPos = getTileCenter(fromTileId, board, cellSize, padding);
    double[] endPos = getTileCenter(toTileId, board, cellSize, padding);

    Path path = new Path();
    path.getElements().add(new MoveTo(startPos[0], startPos[1]));
    path.getElements().add(new LineTo(endPos[0], endPos[1]));

    return path;
  }

  private Path createCurvedPath(int fromTileId, int toTileId, SlBoard board, double cellSize,
      double padding) {
    double[] startPos = getTileCenter(fromTileId, board, cellSize, padding);
    double[] endPos = getTileCenter(toTileId, board, cellSize, padding);

    // Create control points for the curve
    double controlX1 = (startPos[0] + endPos[0]) / 2 + cellSize * 0.5;
    double controlY1 = (startPos[1] + endPos[1]) / 2 - cellSize * 0.5;
    double controlX2 = (startPos[0] + endPos[0]) / 2 - cellSize * 0.5;
    double controlY2 = (startPos[1] + endPos[1]) / 2 + cellSize * 0.5;

    Path path = new Path();
    path.getElements().add(new MoveTo(startPos[0], startPos[1]));
    path.getElements()
        .add(new CubicCurveTo(controlX1, controlY1, controlX2, controlY2, endPos[0], endPos[1]));

    return path;
  }

  private Path createArcPath(int fromTileId, int toTileId, SlBoard board, double cellSize,
      double padding) {
    double[] startPos = getTileCenter(fromTileId, board, cellSize, padding);
    double[] endPos = getTileCenter(toTileId, board, cellSize, padding);

    // Create a high arc for bounce back
    double midX = (startPos[0] + endPos[0]) / 2;
    double controlY = Math.min(startPos[1], endPos[1]) - cellSize;

    Path path = new Path();
    path.getElements().add(new MoveTo(startPos[0], startPos[1]));
    path.getElements().add(new CubicCurveTo(midX, controlY, midX, controlY, endPos[0], endPos[1]));

    return path;
  }

  private double[] getTileCenter(int tileId, SlBoard board, double cellSize, double padding) {
    int[] coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(tileId, board.getRows(),
        board.getColumns());

    double x = coords[1] * cellSize + padding + cellSize * 0.5;
    double y = coords[0] * cellSize + padding + cellSize * 0.5;

    return new double[]{x, y};
  }

  private void queueAnimation(Token token, Path path, Duration duration) {
    if (path == null) {
      return;
    }

    // Initialize animation queue for this token if needed
    if (!animationQueues.containsKey(token)) {
      animationQueues.put(token, new LinkedList<>());
      isAnimating.put(token, false);
    }

    Queue<PathTransition> queue = animationQueues.get(token);

    // Create and add the animation to the queue
    PathTransition transition = new PathTransition(duration, path, token);
    queue.add(transition);

    // Start animation if not already animating
    if (!isAnimating.get(token)) {
      playNextAnimation(token);
    }
  }

  private void playNextAnimation(Token token) {
    Queue<PathTransition> queue = animationQueues.get(token);

    if (queue.isEmpty()) {
      isAnimating.put(token, false);
      return;
    }

    isAnimating.put(token, true);
    PathTransition nextAnimation = queue.poll();

    // When animation finishes, play the next one in queue
    nextAnimation.setOnFinished(e -> playNextAnimation(token));
    nextAnimation.play();
  }

  public void clearAllAnimations() {
    for (Queue<PathTransition> queue : animationQueues.values()) {
      queue.clear();
    }
    animationQueues.clear();
    isAnimating.clear();
  }
}
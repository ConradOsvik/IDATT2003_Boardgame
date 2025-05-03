package edu.ntnu.stud.boardgame.snakesandladders.view.components;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class PlayerToken extends Group {

  private final SlPlayer player;
  private final Circle circle;
  private final Label label;

  public PlayerToken(SlPlayer player, double size) {
    this.player = player;

    // Create the token circle with the player's color
    circle = new Circle(size / 2);
    circle.setFill(player.getColor());
    circle.setStroke(Color.BLACK);
    circle.setStrokeWidth(1);

    // Create label with player's initial
    String initial = player.getName().substring(0, 1).toUpperCase();
    label = new Label(initial);
    label.setFont(Font.font("Arial", FontWeight.BOLD, size * 0.6));
    label.setTextFill(Color.WHITE);
    label.setTranslateX(-size / 4);
    label.setTranslateY(-size / 3);

    // Add components to the group
    getChildren().addAll(circle, label);
  }

  public SlPlayer getPlayer() {
    return player;
  }

  public void positionAtTile(int tileId, SlBoard board, double cellSize, double padding) {
    if (tileId <= 0) {
      return;
    }

    int[] coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(
        tileId, board.getRows(), board.getColumns());

    double x = coords[1] * cellSize + padding + cellSize / 2;
    double y = coords[0] * cellSize + padding + cellSize / 2;

    setTranslateX(x);
    setTranslateY(y);
  }

  public void animateMove(int fromTileId, int toTileId, SlBoard board, double cellSize,
      double padding) {
    SequentialTransition sequence = new SequentialTransition();

    // Create a path for normal movement
    Path normalPath = createMovementPath(fromTileId, toTileId, board, cellSize, padding);
    if (normalPath != null) {
      PathTransition pathTransition = new PathTransition();
      pathTransition.setDuration(Duration.seconds(0.8));
      pathTransition.setPath(normalPath);
      pathTransition.setNode(this);
      sequence.getChildren().add(pathTransition);
    }

    // Check if landing on snake or ladder
    if (board.getSnakes().containsKey(toTileId)) {
      int tailTileId = board.getSnakes().get(toTileId);
      Path snakePath = createSnakePath(toTileId, tailTileId, board, cellSize, padding);

      PathTransition snakeTransition = new PathTransition();
      snakeTransition.setDuration(Duration.seconds(0.5));
      snakeTransition.setPath(snakePath);
      snakeTransition.setNode(this);
      sequence.getChildren().add(snakeTransition);
    } else if (board.getLadders().containsKey(toTileId)) {
      int topTileId = board.getLadders().get(toTileId);
      Path ladderPath = createLadderPath(toTileId, topTileId, board, cellSize, padding);

      PathTransition ladderTransition = new PathTransition();
      ladderTransition.setDuration(Duration.seconds(0.5));
      ladderTransition.setPath(ladderPath);
      ladderTransition.setNode(this);
      sequence.getChildren().add(ladderTransition);
    }

    sequence.play();
  }

  private Path createMovementPath(int fromTileId, int toTileId, SlBoard board, double cellSize,
      double padding) {
    Path path = new Path();

    int rows = board.getRows();
    int cols = board.getColumns();

    // Starting position
    int[] startCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(fromTileId, rows,
        cols);
    double startX = startCoords[1] * cellSize + padding + cellSize / 2;
    double startY = startCoords[0] * cellSize + padding + cellSize / 2;

    path.getElements().add(new MoveTo(startX, startY));

    // Add line segments for each step
    for (int currentTileId = fromTileId + 1; currentTileId <= toTileId; currentTileId++) {
      int[] coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(currentTileId, rows,
          cols);
      double x = coords[1] * cellSize + padding + cellSize / 2;
      double y = coords[0] * cellSize + padding + cellSize / 2;

      path.getElements().add(new LineTo(x, y));
    }

    return path;
  }

  private Path createSnakePath(int headTileId, int tailTileId, SlBoard board, double cellSize,
      double padding) {
    Path path = new Path();

    int rows = board.getRows();
    int cols = board.getColumns();

    int[] headCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(headTileId, rows,
        cols);
    int[] tailCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(tailTileId, rows,
        cols);

    double headX = headCoords[1] * cellSize + padding + cellSize / 2;
    double headY = headCoords[0] * cellSize + padding + cellSize / 2;
    double tailX = tailCoords[1] * cellSize + padding + cellSize / 2;
    double tailY = tailCoords[0] * cellSize + padding + cellSize / 2;

    path.getElements().add(new MoveTo(headX, headY));

    // Create a curved path for sliding down the snake
    double controlX1 = (headX + tailX) / 2 + 30;
    double controlY1 = (headY + tailY) / 2 - 30;
    double controlX2 = (headX + tailX) / 2 - 30;
    double controlY2 = (headY + tailY) / 2 + 30;

    path.getElements().add(new CubicCurveTo(
        controlX1, controlY1,
        controlX2, controlY2,
        tailX, tailY
    ));

    return path;
  }

  private Path createLadderPath(int bottomTileId, int topTileId, SlBoard board, double cellSize,
      double padding) {
    Path path = new Path();

    int rows = board.getRows();
    int cols = board.getColumns();

    int[] bottomCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(bottomTileId, rows,
        cols);
    int[] topCoords = BoardCoordinateConverter.calculateTileCoordinatesFromId(topTileId, rows,
        cols);

    double bottomX = bottomCoords[1] * cellSize + padding + cellSize / 2;
    double bottomY = bottomCoords[0] * cellSize + padding + cellSize / 2;
    double topX = topCoords[1] * cellSize + padding + cellSize / 2;
    double topY = topCoords[0] * cellSize + padding + cellSize / 2;

    path.getElements().add(new MoveTo(bottomX, bottomY));
    path.getElements().add(new LineTo(topX, topY));

    return path;
  }
}
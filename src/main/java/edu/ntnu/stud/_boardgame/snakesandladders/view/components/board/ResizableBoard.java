package edu.ntnu.stud.boardgame.snakesandladders.view.components.board;

import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class ResizableBoard extends Pane {

  public static final int CELL_SIZE = 60;
  public static final int PADDING = 20;

  private final Canvas canvas;
  private final BoardRenderer boardRenderer;

  private SlBoard board;
  private double currentCellSize;
  private double currentPadding;

  private ResizableBoardChangeListener changeListener;

  public ResizableBoard() {
    this.canvas = new Canvas();
    this.boardRenderer = new BoardRenderer();

    getChildren().add(canvas);

    setupCanvasBinding();
  }

  private void setupCanvasBinding() {
    canvas.widthProperty().bind(widthProperty());
    canvas.heightProperty().bind(heightProperty());

    canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
      calculateScaleFactors();
      renderBoard();
    });

    canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
      calculateScaleFactors();
      renderBoard();
    });
  }

  private void calculateScaleFactors() {
    if (canvas.getWidth() <= 0 || canvas.getHeight() <= 0 || board == null) {
      return;
    }

    double baseWidth = board.getColumns() * CELL_SIZE + 2 * PADDING;
    double baseHeight = board.getRows() * CELL_SIZE + 2 * PADDING;

    double scaleX = canvas.getWidth() / baseWidth;
    double scaleY = canvas.getHeight() / baseHeight;

    double scaleFactor = Math.min(scaleX, scaleY);

    currentCellSize = CELL_SIZE * scaleFactor;
    currentPadding = PADDING * scaleFactor;

    if (changeListener != null) {
      changeListener.onScaleFactorsChanged(currentCellSize, currentPadding);
    }
  }

  public void setBoard(SlBoard board) {
    this.board = board;
    calculateScaleFactors();
    renderBoard();
  }

  public void renderBoard() {
    if (board != null && canvas.getWidth() > 0 && canvas.getHeight() > 0) {
      boardRenderer.render(canvas, board, currentCellSize, currentPadding);
    }
  }

  public SlBoard getBoard() {
    return board;
  }

  public double getCurrentCellSize() {
    return currentCellSize;
  }

  public double getCurrentPadding() {
    return currentPadding;
  }

  public void setChangeListener(ResizableBoardChangeListener listener) {
    this.changeListener = listener;
  }

  public interface ResizableBoardChangeListener {

    void onScaleFactorsChanged(double cellSize, double padding);
  }
}
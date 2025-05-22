package edu.ntnu.stud.boardgame.view.components;

import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.view.components.piece.PieceAnimation;
import edu.ntnu.stud.boardgame.view.components.piece.PlayerPiece;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Abstract base class for game board implementations.
 * Provides common functionality for rendering game boards and managing player
 * pieces.
 * Extends {@link StackPane} to layer the board canvas and player pieces.
 *
 * @see GameBoardInterface
 * @see Canvas
 */
public abstract class AbstractGameBoard extends StackPane implements GameBoardInterface {

  protected final Canvas boardCanvas;
  protected final Pane piecesLayer;
  protected final Map<Player, PlayerPiece> playerPieces = new HashMap<>();
  protected final PieceAnimation pieceAnimation;

  protected Board board;
  protected double cellSize;
  protected double padding = 20;
  protected boolean needsRedraw = true;

  /**
   * <p>
   * Initializes the game board with a canvas and pieces layer.
   * </p>
   * <p>
   * Sets up basic layout and resize listeners.
   * </p>
   */
  protected AbstractGameBoard() {
    boardCanvas = new Canvas();
    piecesLayer = new Pane();
    pieceAnimation = new PieceAnimation();

    setMinSize(400, 400);

    boardCanvas.widthProperty().bind(widthProperty());
    boardCanvas.heightProperty().bind(heightProperty());
    piecesLayer.setPickOnBounds(false);

    setupResize();
    getChildren().addAll(boardCanvas, piecesLayer);
  }

  protected void setupResize() {
    ChangeListener<Number> resizeListener = (obs, oldVal, newVal) -> {
      if (newVal.doubleValue() > 0) {
        needsRedraw = true;
        calculateCellSize();
        Platform.runLater(this::drawBoard);
      }
    };

    widthProperty().addListener(resizeListener);
    heightProperty().addListener(resizeListener);
  }

  /**
   * <p>
   * Sets the game board model and triggers a redraw.
   * </p>
   *
   * @param board The board model to use
   */
  @Override
  public void setBoard(Board board) {
    this.board = board;
    needsRedraw = true;
    calculateCellSize();
    drawBoard();
  }

  /**
   * <p>
   * Updates a player's position on the board.
   * </p>
   * <p>
   * Creates a new piece for the player if one doesn't exist.
   * </p>
   *
   * @param player The player to update
   * @param tile   The tile to move to
   */
  @Override
  public void updatePlayerPosition(Player player, Tile tile) {
    if (player == null) {
      return;
    }

    PlayerPiece piece = playerPieces.computeIfAbsent(player, p -> {
      PlayerPiece newPiece = new PlayerPiece(p);
      piecesLayer.getChildren().add(newPiece);
      newPiece.updateSize(Math.max(15, cellSize * 0.6));
      return newPiece;
    });

    if (tile == null || tile.getTileId() == 0) {
      piece.setVisible(false);
    } else {
      piece.setVisible(true);
      positionPieceAtTile(piece, tile);
    }
  }

  /**
   * <p>
   * Animates a player's piece moving from one tile to another.
   * </p>
   * <p>
   * Handles visibility and movement animation for the piece.
   * </p>
   *
   * @param player   The player whose piece to animate
   * @param fromTile The starting tile
   * @param toTile   The destination tile
   */
  @Override
  public void animatePlayerMove(Player player, Tile fromTile, Tile toTile) {
    if (player == null) {
      return;
    }

    PlayerPiece piece = playerPieces.get(player);
    if (piece == null) {
      return;
    }

    if (fromTile == null || fromTile.getTileId() == 0) {
      piece.setVisible(toTile != null && toTile.getTileId() != 0);
      if (piece.isVisible()) {
        positionPieceAtTile(piece, toTile);
      }
      return;
    }

    if (toTile == null || toTile.getTileId() == 0) {
      piece.setVisible(false);
      return;
    }

    int steps = Math.abs(toTile.getTileId() - fromTile.getTileId());

    piece.setVisible(true);
    pieceAnimation.animateRegularMove(piece, fromTile, toTile, cellSize, padding, steps);
  }

  /**
   * <p>
   * Removes all player pieces from the board.
   * </p>
   * <p>
   * Clears all animations and piece references.
   * </p>
   */
  @Override
  public void clearPlayerPieces() {
    pieceAnimation.clearAllAnimations();
    piecesLayer.getChildren().clear();
    playerPieces.clear();
  }

  /**
   * <p>
   * Forces a redraw of the game board.
   * </p>
   */
  @Override
  public void refreshBoard() {
    needsRedraw = true;
    drawBoard();
  }

  protected abstract void calculateCellSize();

  protected abstract void drawBoard();

  protected void positionPieceAtTile(PlayerPiece piece, Tile tile) {
    if (tile == null || tile.getRow() == null || tile.getColumn() == null) {
      return;
    }

    double x = tile.getColumn() * cellSize + padding + cellSize / 2;
    double y = tile.getRow() * cellSize + padding + cellSize / 2;

    double pieceSize = piece.getCurrentSize();
    piece.setTranslateX(x - pieceSize / 2);
    piece.setTranslateY(y - pieceSize / 2);
  }

  protected void updateAllPiecePositions() {
    double pieceSize = Math.max(15, cellSize * 0.6);
    playerPieces.values().forEach(piece -> piece.updateSize(pieceSize));

    playerPieces.forEach((player, piece) -> {
      if (player.getCurrentTile() != null) {
        if (player.getCurrentTile().getTileId() == 0) {
          piece.setVisible(false);
        } else {
          piece.setVisible(true);
          positionPieceAtTile(piece, player.getCurrentTile());
        }
      } else {
        piece.setVisible(false);
      }
    });
  }
}
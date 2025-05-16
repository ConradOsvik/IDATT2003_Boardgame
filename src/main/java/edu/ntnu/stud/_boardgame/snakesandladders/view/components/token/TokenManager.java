package edu.ntnu.stud.boardgame.snakesandladders.view.components.token;

import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.snakesandladders.events.BounceBackEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.SnakeEncounteredEvent;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.util.BoardCoordinateConverter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.Pane;

public class TokenManager extends Pane {

  private static final Logger LOGGER = Logger.getLogger(TokenManager.class.getName());
  private final Map<SlPlayer, Token> playerTokens = new HashMap<>();
  private final TokenAnimation tokenAnimation = new TokenAnimation();

  private SlBoard board;
  private double cellSize;
  private double padding;

  public TokenManager() {
    setPickOnBounds(false);
  }

  public void updateLayout(SlBoard board, double cellSize, double padding) {
    this.board = board;
    this.cellSize = cellSize;
    this.padding = padding;

    try {
      // Update token sizes based on cell size
      double tokenSize = cellSize * 0.6;
      playerTokens.values().forEach(token -> token.updateSize(tokenSize));

      // Update token positions
      updateAllPositions();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error updating token layout", e);
      ErrorDialog.getInstance().showError("Display Error",
          "Failed to update token positions: " + e.getMessage());
    }
  }

  private void updateAllPositions() {
    if (board == null) {
      return;
    }

    for (Map.Entry<SlPlayer, Token> entry : playerTokens.entrySet()) {
      SlPlayer player = entry.getKey();
      Token token = entry.getValue();

      if (player.getCurrentTile() != null && player.getCurrentTile().getTileId() > 0) {
        positionTokenAtTile(token, player.getCurrentTile().getTileId());
        token.setVisible(true);
      } else {
        token.setVisible(false);
      }
    }
  }

  private void positionTokenAtTile(Token token, int tileId) {
    if (tileId <= 0) {
      return;
    }

    try {
      int[] coords = BoardCoordinateConverter.calculateTileCoordinatesFromId(
          tileId, board.getRows(), board.getColumns());

      double x = coords[1] * cellSize + padding + cellSize * 0.5;
      double y = coords[0] * cellSize + padding + cellSize * 0.5;

      token.setTranslateX(x);
      token.setTranslateY(y);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error positioning token", e);
    }
  }

  public void addToken(SlPlayer player) {
    if (board == null) {
      return;
    }

    try {
      // Create a new token renderer for this player
      Token token = new Token(player.getTokenId());
      token.updateSize(cellSize * 0.6);
      playerTokens.put(player, token);

      // Add token to the display
      getChildren().add(token);

      // Position token if player is on a tile
      if (player.getCurrentTile() != null && player.getCurrentTile().getTileId() > 0) {
        positionTokenAtTile(token, player.getCurrentTile().getTileId());
        token.setVisible(true);
      } else {
        token.setVisible(false);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error adding token for player", e);
      ErrorDialog.getInstance().showError("Display Error",
          "Failed to create token for player: " + e.getMessage());
    }
  }

  public void handlePlayerMovement(PlayerMovedEvent event) {
    if (board == null) {
      return;
    }

    try {
      SlPlayer player = (SlPlayer) event.getPlayer();
      Tile fromTile = event.getFromTile();
      Tile toTile = event.getToTile();

      Token token = playerTokens.get(player);
      if (token == null || toTile == null) {
        return;
      }

      // If starting position is null (player just added to board), just position directly
      if (fromTile == null || fromTile.getTileId() == 0) {
        token.setVisible(true);
        positionTokenAtTile(token, toTile.getTileId());
        return;
      }

      // Determine animation type based on event type
      switch (event) {
        case BounceBackEvent bounceBackEvent ->
            tokenAnimation.animateBounceBack(token, fromTile.getTileId(), toTile.getTileId(),
                board, cellSize, padding);
        case SnakeEncounteredEvent snakeEncounteredEvent ->
            tokenAnimation.animateSnakeSlide(token, fromTile.getTileId(), toTile.getTileId(),
                board, cellSize, padding);
        case LadderClimbedEvent ladderClimbedEvent ->
            tokenAnimation.animateLadderClimb(token, fromTile.getTileId(), toTile.getTileId(),
                board, cellSize, padding);
        default ->
            tokenAnimation.animateRegularMove(token, fromTile.getTileId(), toTile.getTileId(),
                board, cellSize, padding, event.getSteps());
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error handling player movement", e);
      ErrorDialog.getInstance().showError("Animation Error",
          "Failed to animate player movement: " + e.getMessage());
    }
  }

  public void resetTokenPositions() {
    playerTokens.values().forEach(token -> token.setVisible(false));
  }

  public void clear() {
    playerTokens.clear();
    getChildren().clear();
    tokenAnimation.clearAllAnimations();
  }
}
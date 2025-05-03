package edu.ntnu.stud.boardgame.snakesandladders.view.components;

import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.layout.Pane;

public class TokenManager extends Pane {

  private final Map<SlPlayer, PlayerToken> playerTokens = new HashMap<>();

  private SlBoard board;
  private double cellSize;
  private double padding;

  public TokenManager() {
    // Set background transparent so the board shows through
    setPickOnBounds(false);
  }

  public void updateLayout(SlBoard board, double cellSize, double padding) {
    this.board = board;
    this.cellSize = cellSize;
    this.padding = padding;

    // Update the position of all tokens
    updateAllPositions();
  }

  private void updateAllPositions() {
    if (board == null) {
      return;
    }

    for (Map.Entry<SlPlayer, PlayerToken> entry : playerTokens.entrySet()) {
      SlPlayer player = entry.getKey();
      PlayerToken token = entry.getValue();

      if (player.getCurrentTile() != null) {
        int tileId = player.getCurrentTile().getTileId();
        token.positionAtTile(tileId, board, cellSize, padding);
      }
    }
  }

  public void addPlayer(SlPlayer player) {
    if (board == null) {
      return;
    }

    // Create a new player token
    PlayerToken token = new PlayerToken(player, cellSize * 0.6);
    playerTokens.put(player, token);

    // Add token to the pane
    getChildren().add(token);

    // Position the token if possible
    if (player.getCurrentTile() != null) {
      token.positionAtTile(player.getCurrentTile().getTileId(), board, cellSize, padding);
    }
  }

  public void playerMoved(SlPlayer player, Tile fromTile, Tile toTile) {
    if (board == null) {
      return;
    }

    PlayerToken token = playerTokens.get(player);
    if (token != null && fromTile != null && toTile != null) {
      token.animateMove(fromTile.getTileId(), toTile.getTileId(), board, cellSize, padding);
    }
  }

  public void clear() {
    playerTokens.clear();
    getChildren().clear();
  }
}
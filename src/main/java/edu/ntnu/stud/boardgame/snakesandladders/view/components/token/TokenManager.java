package edu.ntnu.stud.boardgame.snakesandladders.view.components.token;

import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.snakesandladders.events.SlPlayerMovedEvent;
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
    setPickOnBounds(false);
  }

  public void updateLayout(SlBoard board, double cellSize, double padding) {
    this.board = board;
    this.cellSize = cellSize;
    this.padding = padding;

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
        if (tileId > 0) {
          token.positionAtTile(tileId, board, cellSize, padding);
        } else {
          token.setVisible(false);
        }
      }
    }
  }

  public void addPlayer(SlPlayer player) {
    if (board == null) {
      return;
    }

    PlayerToken token = new PlayerToken(cellSize * 0.6, player.getTokenId());
    playerTokens.put(player, token);

    getChildren().add(token);
  }

  private String getTokenForPlayer(SlPlayer player) {
    String playerNumber = String.valueOf(player.hashCode() % 4 + 1);

    return "token_" + playerNumber;
  }

  public void playerMoved(SlPlayerMovedEvent event) {
    if (board == null) {
      return;
    }

    SlPlayer player = event.getPlayer();
    Tile fromTile = event.getFromTile();
    Tile toTile = event.getToTile();
    boolean isSnakeMove = event.isSnakeMove();
    boolean isLadderMove = event.isLadderMove();

    PlayerToken token = playerTokens.get(player);
    if (token == null || toTile == null) {
      return;
    }

    if (fromTile == null || fromTile.getTileId() == 0) {
      token.setVisible(true);
      token.positionAtTile(toTile.getTileId(), board, cellSize, padding);
      return;
    }

    if (fromTile.getTileId() == SlBoard.NUM_TILES && toTile.getTileId() < SlBoard.NUM_TILES) {
      token.animateBounceBackMove(fromTile.getTileId(), toTile.getTileId(),
          board, cellSize, padding);
    } else if (isSnakeMove) {
      token.animateSnakeMove(fromTile.getTileId(), toTile.getTileId(),
          board, cellSize, padding);
    } else if (isLadderMove) {
      token.animateLadderMove(fromTile.getTileId(), toTile.getTileId(),
          board, cellSize, padding);
    } else {
      token.animateRegularMove(fromTile.getTileId(), toTile.getTileId(),
          board, cellSize, padding);
    }
  }

  public void clear() {
    playerTokens.clear();
    getChildren().clear();
  }

  public void resetTokenPositions() {
    for (PlayerToken token : playerTokens.values()) {
      token.setVisible(false);
    }
  }
}
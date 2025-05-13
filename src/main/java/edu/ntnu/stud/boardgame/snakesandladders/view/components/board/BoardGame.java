package edu.ntnu.stud.boardgame.snakesandladders.view.components.board;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameRestartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.events.BounceBackEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.snakesandladders.events.SnakeEncounteredEvent;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import edu.ntnu.stud.boardgame.snakesandladders.view.components.token.TokenManager;
import javafx.scene.layout.StackPane;

public class BoardGame extends GameComponent<StackPane> {

  private final ResizableBoard resizableBoard;
  private final TokenManager tokenManager;

  public BoardGame(SlGameController controller) {
    super(controller, new StackPane());
    this.resizableBoard = new ResizableBoard();
    this.tokenManager = new TokenManager();

    initializeLayout();

    resizableBoard.setChangeListener((cellSize, padding) -> {
      if (resizableBoard.getBoard() != null) {
        tokenManager.updateLayout(resizableBoard.getBoard(), cellSize, padding);
      }
    });
  }

  private void initializeLayout() {
    this.getNode().setMinWidth(0);
    this.getNode().setMinHeight(0);
    this.getNode().getChildren().addAll(resizableBoard, tokenManager);

    resizableBoard.prefWidthProperty().bind(this.getNode().widthProperty());
    resizableBoard.prefHeightProperty().bind(this.getNode().heightProperty());

    tokenManager.prefWidthProperty().bind(this.getNode().widthProperty());
    tokenManager.prefHeightProperty().bind(this.getNode().heightProperty());
  }

  @Override
  public void onGameEvent(GameEvent e) {
    switch (e) {
      case GameCreatedEvent event -> resetBoard((SlBoard) event.getBoard());
      case GameResetEvent event -> resetBoard((SlBoard) event.getBoard());
      case GameRestartedEvent event -> {
        tokenManager.resetTokenPositions();
        updateTokenLayout();
      }
      case PlayerAddedEvent event -> {
        tokenManager.addPlayer((SlPlayer) event.getPlayer());
        updateTokenLayout();
      }
      case SnakeEncounteredEvent event -> {
        tokenManager.playerMoved(event);
      }
      case LadderClimbedEvent event -> {
        tokenManager.playerMoved(event);
      }
      case BounceBackEvent event -> {
        tokenManager.playerMoved(event);
      }
      case PlayerMovedEvent event -> {
        tokenManager.playerMoved(event);
      }
      default -> { /* No action needed */ }
    }
  }

  private void resetBoard(SlBoard newBoard) {
    resizableBoard.setBoard(newBoard);
    tokenManager.clear();
    updateTokenLayout();
  }

  private void updateTokenLayout() {
    if (resizableBoard.getBoard() != null) {
      tokenManager.updateLayout(
          resizableBoard.getBoard(),
          resizableBoard.getCurrentCellSize(),
          resizableBoard.getCurrentPadding()
      );
    }
  }
}
package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.BounceBackEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.observer.event.GameStartedEvent;
import edu.ntnu.stud.boardgame.observer.event.LadderClimbedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerWonEvent;
import edu.ntnu.stud.boardgame.observer.event.SnakeEncounteredEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.laddergame.ControlPanel;
import edu.ntnu.stud.boardgame.view.components.laddergame.GameBoard;
import edu.ntnu.stud.boardgame.view.components.laddergame.PlayerScoreboard;
import edu.ntnu.stud.boardgame.view.components.laddergame.VictoryScreen;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LadderGameView extends BorderPane implements BoardGameObserver {

  private final MainController controller;
  private final GameController gameController;

  private final ControlPanel controlPanel;
  private final PlayerScoreboard scoreboard;
  private final GameBoard gameBoardView;
  private final VictoryScreen victoryScreen;
  private final SoundManager soundManager;

  public LadderGameView(MainController controller, GameController gameController) {
    this.controller = controller;
    this.gameController = gameController;
    this.gameController.registerObserver(this);

    this.controlPanel = new ControlPanel(gameController);
    this.scoreboard = new PlayerScoreboard();
    this.gameBoardView = new GameBoard();
    this.victoryScreen = new VictoryScreen(gameController);
    this.soundManager = new SoundManager();

    initializeLayout();
    victoryScreen.setVisible(false);
  }

  private void initializeLayout() {
    VBox leftPanel = new VBox(20);
    leftPanel.setPadding(new Insets(15));
    leftPanel.setPrefWidth(300);
    leftPanel.getChildren().addAll(controlPanel, scoreboard);
    VBox.setVgrow(scoreboard, Priority.ALWAYS);

    StackPane gameArea = new StackPane();
    gameArea.setAlignment(Pos.CENTER);
    gameArea.getChildren().add(gameBoardView);
    gameArea.getChildren().add(victoryScreen);
    StackPane.setAlignment(victoryScreen, Pos.CENTER);

    Button menuButton = new ButtonBuilder()
        .text("Back to Menu")
        .styleClass("secondary-button")
        .onClick(e -> returnToMenu())
        .build();

    HBox bottomBar = new HBox(menuButton);
    bottomBar.setAlignment(Pos.CENTER);
    bottomBar.setPadding(new Insets(10));

    setLeft(leftPanel);
    setCenter(gameArea);
    setBottom(bottomBar);
  }

  private void returnToMenu() {
    controller.showGameSelectionView();
  }

  private void showVictoryScreen(Player winner) {
    soundManager.playSound("victory");
    victoryScreen.showVictory(winner);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    Platform.runLater(() -> {
      if (event instanceof GameStartedEvent startedEvent) {
        controlPanel.setDiceDisabled(true);
        controlPanel.updateCurrentPlayer(startedEvent.getCurrentPlayer());
        updateUI(startedEvent.getBoard(), startedEvent.getPlayers());
        victoryScreen.setVisible(false);
        scoreboard.updatePlayers(gameController.getGame().getPlayers());
      } else if (event instanceof DiceRolledEvent diceEvent) {
        soundManager.playSound("dice_roll");
        controlPanel.updateDiceValue(diceEvent.getValue());
      } else if (event instanceof LadderClimbedEvent ladderEvent) {
        soundManager.playSound("ladder");
        gameBoardView.animatePlayerLadderClimb(ladderEvent.getPlayer(),
            ladderEvent.getFromTile(),
            ladderEvent.getToTile());
      } else if (event instanceof SnakeEncounteredEvent snakeEvent) {
        soundManager.playSound("snake");
        gameBoardView.animatePlayerSnakeSlide(snakeEvent.getPlayer(),
            snakeEvent.getFromTile(),
            snakeEvent.getToTile());
      } else if (event instanceof BounceBackEvent bounceEvent) {
        soundManager.playSound("bounce_back");
        gameBoardView.animatePlayerBounceBack(bounceEvent.getPlayer(),
            bounceEvent.getFromTile(),
            bounceEvent.getToTile());
      } else if (event instanceof PlayerMovedEvent moveEvent) {
        soundManager.playSound("move");
        gameBoardView.animatePlayerMove(moveEvent.getPlayer(),
            moveEvent.getFromTile(),
            moveEvent.getToTile());
      } else if (event instanceof TurnChangedEvent turnEvent) {
        Player currentPlayer = turnEvent.getCurrentPlayer();
        controlPanel.updateCurrentPlayer(currentPlayer);
        scoreboard.highlightCurrentPlayer(currentPlayer);
      } else if (event instanceof PlayerWonEvent wonEvent) {
        controlPanel.setDiceDisabled(true);
        Player winner = wonEvent.getWinner();
        if (winner != null) {
          showVictoryScreen(winner);
        }
      } else if (event instanceof GameEndedEvent endedEvent) {
        controlPanel.setDiceDisabled(true);
        Player winner = endedEvent.getWinner();
        if (winner != null) {
          showVictoryScreen(winner);
        }
      }
    });
  }

  private void updateUI(Board board, List<Player> players) {
    gameBoardView.setBoard(board);
    for (Player player : players) {
      if (player.getCurrentTile() != null) {
        gameBoardView.updatePlayerPosition(player, player.getCurrentTile());
      }
    }
  }

  private class SoundManager {

    public void playSound(String name) {
    }
  }
}
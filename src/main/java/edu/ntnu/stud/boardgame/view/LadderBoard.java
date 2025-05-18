package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.game.LadderGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
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
import java.net.URL;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class LadderBoard extends BorderPane implements BoardGameObserver {

  private final MainController mainController;
  private final GameController gameController;

  private final ControlPanel controlPanel;
  private final PlayerScoreboard scoreboard;
  private final GameBoard gameBoard;
  private final VictoryScreen victoryScreen;

//  private final Map<String, AudioClip> sounds = new HashMap<>();

  public LadderBoard(MainController mainController, GameController gameController) {
    this.mainController = mainController;
    this.gameController = gameController;

    mainController.registerObserver(this);

    this.controlPanel = new ControlPanel(gameController);
    this.scoreboard = new PlayerScoreboard();
    this.gameBoard = new GameBoard();
    this.victoryScreen = new VictoryScreen();

    loadAudioClips();

    initializeLayout();

    victoryScreen.setVisible(false);
  }

  private void initializeLayout() {
    VBox leftPanel = new VBox(20);
    leftPanel.setPadding(new Insets(15));
    leftPanel.setPrefWidth(250);
    leftPanel.getChildren().addAll(controlPanel, scoreboard);
    VBox.setVgrow(scoreboard, Priority.ALWAYS);

    BorderPane centerPanel = new BorderPane();
    centerPanel.setCenter(gameBoard);
    centerPanel.setStyle("-fx-background-color: #f0f0f0;");

    Button menuButton = new ButtonBuilder()
        .text("Back to Menu")
        .styleClass("menu-button")
        .onClick(e -> mainController.showGameSelectionView())
        .build();

    HBox bottomBar = new HBox(menuButton);
    bottomBar.setAlignment(Pos.CENTER);
    bottomBar.setPadding(new Insets(10));

    setLeft(leftPanel);
    setCenter(centerPanel);
    setBottom(bottomBar);

    centerPanel.getChildren().add(victoryScreen);
  }

  private void loadAudioClips() {
    try {
      loadSound("dice_roll", "/sounds/dice_roll.wav");
      loadSound("move", "/sounds/move.wav");
      loadSound("ladder", "/sounds/ladder.wav");
      loadSound("snake", "/sounds/snake.wav");
      loadSound("victory", "/sounds/victory.wav");
    } catch (Exception e) {
      System.err.println("Failed to load audio clips: " + e.getMessage());
    }
  }

  private void loadSound(String name, String resourcePath) {
    URL resource = getClass().getResource(resourcePath);
    if (resource != null) {
//      AudioClip clip = new AudioClip(resource.toExternalForm());
//      sounds.put(name, clip);
    } else {
      System.err.println("Could not find sound resource: " + resourcePath);
    }
  }

  private void playSound(String name) {
//    AudioClip clip = sounds.get(name);
//    if (clip != null) {
//      clip.play();
//    }
  }

  public void updateBoard() {
    LadderGame game = (LadderGame) gameController.getGame();
    if (game != null) {
      Board board = game.getBoard();
      gameBoard.setBoard(board);

      for (Player player : game.getPlayers()) {
        Tile currentTile = player.getCurrentTile();
        if (currentTile != null) {
          gameBoard.updatePlayerPosition(player, currentTile);
        }
      }
    }
  }

  public void showVictoryScreen(Player winner) {
    playSound("victory");
    victoryScreen.showVictory(winner);
    victoryScreen.setVisible(true);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    Platform.runLater(() -> {
      if (event instanceof GameStartedEvent) {
        updateBoard();
        victoryScreen.setVisible(false);
        scoreboard.updatePlayers(gameController.getGame().getPlayers());
      } else if (event instanceof DiceRolledEvent diceEvent) {
        playSound("dice_roll");
        controlPanel.updateDiceValue(diceEvent.getValue());
      } else if (event instanceof LadderClimbedEvent ladderEvent) {
        playSound("ladder");
        gameBoard.animatePlayerLadderClimb(ladderEvent.getPlayer(),
            ladderEvent.getFromTile(),
            ladderEvent.getToTile());
      } else if (event instanceof SnakeEncounteredEvent snakeEvent) {
        playSound("snake");
        gameBoard.animatePlayerSnakeSlide(snakeEvent.getPlayer(),
            snakeEvent.getFromTile(),
            snakeEvent.getToTile());
      } else if (event instanceof PlayerMovedEvent moveEvent) {
        playSound("move");
        gameBoard.animatePlayerMove(moveEvent.getPlayer(),
            moveEvent.getFromTile(),
            moveEvent.getToTile());
      } else if (event instanceof TurnChangedEvent turnEvent) {
        Player currentPlayer = gameController.getGame().getCurrentPlayer();
        controlPanel.updateCurrentPlayer(currentPlayer);
        scoreboard.highlightCurrentPlayer(currentPlayer);
      } else if (event instanceof PlayerWonEvent || event instanceof GameEndedEvent) {
        Player winner = gameController.getGame().getWinner();
        if (winner != null) {
          showVictoryScreen(winner);
        }
      }
    });
  }
}
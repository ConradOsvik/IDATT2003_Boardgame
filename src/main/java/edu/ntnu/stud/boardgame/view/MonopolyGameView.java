package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.controller.MonopolyController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import edu.ntnu.stud.boardgame.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.GameEndedEvent;
import edu.ntnu.stud.boardgame.observer.event.GameStartedEvent;
import edu.ntnu.stud.boardgame.observer.event.MoneyTransferEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerBankruptEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.PropertyPurchasedEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import edu.ntnu.stud.boardgame.util.SoundManager;
import edu.ntnu.stud.boardgame.view.components.AudioControlPanel;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import edu.ntnu.stud.boardgame.view.components.laddergame.VictoryScreen;
import edu.ntnu.stud.boardgame.view.components.monopoly.MonopolyBoard;
import edu.ntnu.stud.boardgame.view.components.monopoly.MonopolyPlayerScoreboard;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MonopolyGameView extends BorderPane implements BoardGameObserver {

  private final MainController mainController;
  private final GameController gameController;
  private final MonopolyController monopolyController;

  private final MonopolyBoard gameBoard;
  private final MonopolyPlayerScoreboard playerScoreboard;
  private final VictoryScreen victoryScreen;
  private final SoundManager soundManager;

  // Control panel elements
  private final Label statusLabel;
  private final Button rollDiceButton;
  private final Button buyPropertyButton;

  public MonopolyGameView(MainController mainController, GameController gameController) {
    this.mainController = mainController;
    this.gameController = gameController;
    this.gameController.registerObserver(this);
    this.monopolyController = new MonopolyController(gameController);
    this.soundManager = SoundManager.getInstance();

    this.gameBoard = new MonopolyBoard(monopolyController);
    this.playerScoreboard = new MonopolyPlayerScoreboard(monopolyController);
    this.victoryScreen = new VictoryScreen(gameController);

    this.statusLabel = new LabelBuilder()
        .text("Welcome to Monopoly")
        .styleClass("title")
        .build();

    this.rollDiceButton = new ButtonBuilder()
        .text("Roll Dice")
        .styleClass("action-button")
        .onClick(e -> rollDice())
        .disabled(true)
        .build();

    this.buyPropertyButton = new ButtonBuilder()
        .text("Buy Property")
        .styleClass("action-button")
        .onClick(e -> buyProperty())
        .disabled(true)
        .build();

    initializeLayout();
    victoryScreen.setVisible(false);
  }

  private void initializeLayout() {
    setPadding(new Insets(20));

    VBox controlPanel = new VBox(15);
    controlPanel.setPadding(new Insets(15));
    controlPanel.setSpacing(15);
    controlPanel.getStyleClass().add("card");

    Label titleLabel = new LabelBuilder()
        .text("Monopoly")
        .styleClass("text-h2")
        .build();

    Button restartButton = new ButtonBuilder()
        .text("Restart Game")
        .styleClass("btn-secondary")
        .onClick(e -> restartGame())
        .build();

    controlPanel.getChildren().addAll(
        titleLabel,
        statusLabel,
        rollDiceButton,
        buyPropertyButton,
        restartButton
    );

    VBox leftPanel = new VBox(20);
    leftPanel.setPadding(new Insets(10));
    leftPanel.getChildren().addAll(controlPanel, playerScoreboard, new AudioControlPanel());
    leftPanel.setPrefWidth(250);

    Button menuButton = new ButtonBuilder()
        .text("Back to Menu")
        .styleClass("secondary-button")
        .onClick(e -> mainController.showGameSelectionView())
        .build();

    HBox bottomBar = new HBox(menuButton);
    bottomBar.setAlignment(Pos.CENTER);
    bottomBar.setPadding(new Insets(10));

    StackPane centerPane = new StackPane();
    centerPane.getChildren().addAll(gameBoard, victoryScreen);
    centerPane.setAlignment(Pos.CENTER);

    setLeft(leftPanel);
    setCenter(centerPane);
    setBottom(bottomBar);
  }

  private void rollDice() {
    monopolyController.rollDice();
    buyPropertyButton.setDisable(true);
  }

  private void buyProperty() {
    Player currentPlayer = monopolyController.getGame().getCurrentPlayer();
    if (currentPlayer == null) {
      return;
    }

    Tile currentTile = currentPlayer.getCurrentTile();
    if (currentTile == null) {
      return;
    }

    boolean purchased = monopolyController.buyProperty(currentPlayer, currentTile);

    if (purchased) {
      statusLabel.setText(currentPlayer.getName() + " bought " + currentTile.getName());
      buyPropertyButton.setDisable(true);

      gameBoard.refreshBoard();
      playerScoreboard.updatePlayerMoney(currentPlayer);
    }
  }

  private void restartGame() {
    try {
      gameController.startGame();
      statusLabel.setText("Game restarted");
      buyPropertyButton.setDisable(true);
    } catch (Exception e) {
      statusLabel.setText("Error restarting game");
    }
  }

  private void updateBuyButton(Player player) {
    if (player == null) {
      buyPropertyButton.setDisable(true);
      return;
    }

    Tile currentTile = player.getCurrentTile();
    if (currentTile == null) {
      buyPropertyButton.setDisable(true);
      return;
    }

    boolean canBuy = monopolyController.canBuyProperty(player, currentTile);
    buyPropertyButton.setDisable(!canBuy);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    Platform.runLater(() -> {
      monopolyController.updateGameReference();

      if (!(gameController.getGame() instanceof MonopolyGame)) {
        return;
      }

      MonopolyGame game = (MonopolyGame) gameController.getGame();

      if (event instanceof GameStartedEvent startedEvent) {
        gameBoard.setBoard(startedEvent.getBoard());
        playerScoreboard.updatePlayers(startedEvent.getPlayers());
        statusLabel.setText("Game Started - Roll the Dice");

        Player currentPlayer = startedEvent.getCurrentPlayer();
        playerScoreboard.highlightCurrentPlayer(currentPlayer);
        rollDiceButton.setDisable(false);
        updateBuyButton(currentPlayer);

        for (Player player : startedEvent.getPlayers()) {
          gameBoard.updatePlayerPositions(player, player.getCurrentTile());
        }

        victoryScreen.setVisible(false);

      } else if (event instanceof DiceRolledEvent diceEvent) {
        soundManager.playSound("dice_roll");
        statusLabel.setText(diceEvent.getPlayer().getName() + " rolled " + diceEvent.getValue());

      } else if (event instanceof PlayerMovedEvent moveEvent) {
        soundManager.playSound("move");
        gameBoard.updatePlayerPositions(moveEvent.getPlayer(), moveEvent.getToTile());
        statusLabel.setText(moveEvent.getPlayer().getName() +
            " moved to " + moveEvent.getToTile().getName());

        if (moveEvent.getPlayer().equals(game.getCurrentPlayer())) {
          updateBuyButton(moveEvent.getPlayer());
        }

      } else if (event instanceof TurnChangedEvent turnEvent) {
        Player currentPlayer = turnEvent.getCurrentPlayer();
        statusLabel.setText(currentPlayer.getName() + "'s turn");
        playerScoreboard.highlightCurrentPlayer(currentPlayer);
        rollDiceButton.setDisable(false);
        updateBuyButton(currentPlayer);

      } else if (event instanceof PropertyPurchasedEvent propEvent) {
        soundManager.playSound("freeze"); // Reusing existing sound
        statusLabel.setText(propEvent.getPlayer().getName() +
            " bought " + propEvent.getProperty().getName() +
            " for $" + propEvent.getPrice());

        gameBoard.refreshBoard();
        playerScoreboard.updatePlayerMoney(propEvent.getPlayer());

      } else if (event instanceof MoneyTransferEvent transferEvent) {
        if (transferEvent.getFromPlayer() != null) {
          playerScoreboard.updatePlayerMoney(transferEvent.getFromPlayer());
        }

        if (transferEvent.getToPlayer() != null) {
          playerScoreboard.updatePlayerMoney(transferEvent.getToPlayer());
        }

        if (transferEvent.getToPlayer() == null) {
          soundManager.playSound("snake");
          statusLabel.setText(transferEvent.getFromPlayer().getName() +
              " paid $" + transferEvent.getAmount() +
              " in " + transferEvent.getReason());
        } else if (transferEvent.getFromPlayer() == null) {
          soundManager.playSound("ladder");
          statusLabel.setText(transferEvent.getToPlayer().getName() +
              " received $" + transferEvent.getAmount() +
              " for " + transferEvent.getReason());
        } else {
          soundManager.playSound("bounce");
          statusLabel.setText(transferEvent.getFromPlayer().getName() +
              " paid $" + transferEvent.getAmount() +
              " " + transferEvent.getReason() +
              " to " + transferEvent.getToPlayer().getName());
        }

      } else if (event instanceof PlayerBankruptEvent bankruptEvent) {
        soundManager.playSound("freeze");
        statusLabel.setText(bankruptEvent.getPlayer().getName() + " is bankrupt!");
        playerScoreboard.updatePlayerMoney(bankruptEvent.getPlayer());

      } else if (event instanceof GameEndedEvent endEvent) {
        rollDiceButton.setDisable(true);
        buyPropertyButton.setDisable(true);

        if (endEvent.getWinner() != null) {
          statusLabel.setText(endEvent.getWinner().getName() + " wins the game!");
          victoryScreen.showVictory(endEvent.getWinner());
          soundManager.playSound("victory");
        } else {
          statusLabel.setText("Game over!");
        }
      }
    });
  }
}
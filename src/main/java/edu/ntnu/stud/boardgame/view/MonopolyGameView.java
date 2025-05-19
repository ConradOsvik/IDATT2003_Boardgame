package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.controller.MonopolyController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.observer.GameEvent;
import edu.ntnu.stud.boardgame.observer.event.DiceRolledEvent;
import edu.ntnu.stud.boardgame.observer.event.GameStartedEvent;
import edu.ntnu.stud.boardgame.observer.event.MoneyTransferEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerBankruptEvent;
import edu.ntnu.stud.boardgame.observer.event.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.observer.event.PropertyPurchasedEvent;
import edu.ntnu.stud.boardgame.observer.event.TurnChangedEvent;
import edu.ntnu.stud.boardgame.view.components.AbstractGameView;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import edu.ntnu.stud.boardgame.view.components.monopoly.MonopolyGameBoard;
import edu.ntnu.stud.boardgame.view.components.monopoly.MonopolyPlayerScoreboard;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MonopolyGameView extends AbstractGameView {

  private final MonopolyController monopolyController;
  private final MonopolyGameBoard gameBoard;
  private final MonopolyPlayerScoreboard playerScoreboard;

  private final Label statusLabel;
  private final Button rollDiceButton;
  private final Button buyPropertyButton;

  public MonopolyGameView(MainController mainController, GameController gameController) {
    super(mainController, gameController);
    this.monopolyController = new MonopolyController(gameController);

    this.gameBoard = new MonopolyGameBoard(monopolyController);
    this.playerScoreboard = new MonopolyPlayerScoreboard(monopolyController);

    this.statusLabel = new LabelBuilder()
        .text("Welcome to Monopoly")
        .styleClass("title")
        .wrapText(true)
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
  }

  private void initializeLayout() {
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
    leftPanel.getChildren().addAll(controlPanel, playerScoreboard);
    leftPanel.setPrefWidth(300);

    gameArea.getChildren().add(0, gameBoard);

    setLeft(leftPanel);
  }

  private void rollDice() {
    monopolyController.rollDice();
    buyPropertyButton.setDisable(true);
  }

  private void buyProperty() {
    Player currentPlayer = gameController.getGame().getCurrentPlayer();
    if (currentPlayer == null) {
      return;
    }

    Tile currentTile = currentPlayer.getCurrentTile();
    if (currentTile == null) {
      return;
    }

    boolean purchased = monopolyController.buyProperty(currentTile);

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

    if (monopolyController.isPlayerBankrupt(player)) {
      buyPropertyButton.setDisable(true);
      return;
    }

    Tile currentTile = player.getCurrentTile();
    if (currentTile == null) {
      buyPropertyButton.setDisable(true);
      return;
    }

    boolean canBuy = monopolyController.canBuyProperty(currentTile);
    buyPropertyButton.setDisable(!canBuy);
  }

  private void handleGameStarted(GameStartedEvent event) {
    gameBoard.clearPlayerPieces();

    gameBoard.setBoard(event.getBoard());
    playerScoreboard.updatePlayers(event.getPlayers());
    statusLabel.setText("Game Started - Roll the Dice");

    Player currentPlayer = event.getCurrentPlayer();
    playerScoreboard.highlightCurrentPlayer(currentPlayer);
    rollDiceButton.setDisable(false);
    updateBuyButton(currentPlayer);
    victoryScreen.setVisible(false);

    for (Player player : event.getPlayers()) {
      if (player.getCurrentTile() != null) {
        gameBoard.updatePlayerPosition(player, player.getCurrentTile());
      }
    }
  }

  private void handleDiceRolled(DiceRolledEvent event) {
    soundManager.playSound("dice_roll");
    statusLabel.setText(event.getPlayer().getName() + " rolled " + event.getValue());
  }

  private void handlePlayerMoved(PlayerMovedEvent event) {
    soundManager.playSound("move");
    gameBoard.animatePlayerMove(event.getPlayer(), event.getFromTile(), event.getToTile());
    statusLabel.setText(event.getPlayer().getName() +
        " moved to " + event.getToTile().getName());

    if (event.getPlayer().equals(gameController.getGame().getCurrentPlayer())) {
      updateBuyButton(event.getPlayer());
    }
  }

  private void handleTurnChanged(TurnChangedEvent event) {
    Player currentPlayer = event.getCurrentPlayer();
    statusLabel.setText(currentPlayer.getName() + "'s turn");
    playerScoreboard.highlightCurrentPlayer(currentPlayer);

    rollDiceButton.setDisable(monopolyController.isPlayerBankrupt(currentPlayer));
    updateBuyButton(currentPlayer);
  }

  private void handlePropertyPurchased(PropertyPurchasedEvent event) {
    soundManager.playSound("freeze");
    statusLabel.setText(event.getPlayer().getName() +
        " bought " + event.getProperty().getName() +
        " for $" + event.getPrice());

    gameBoard.refreshBoard();
    playerScoreboard.updatePlayerMoney(event.getPlayer());
  }

  private void handleMoneyTransfer(MoneyTransferEvent event) {
    if (event.getFromPlayer() != null) {
      playerScoreboard.updatePlayerMoney(event.getFromPlayer());
    }

    if (event.getToPlayer() != null) {
      playerScoreboard.updatePlayerMoney(event.getToPlayer());
    }

    if (event.getToPlayer() == null) {
      soundManager.playSound("snake");
      statusLabel.setText(event.getFromPlayer().getName() +
          " paid $" + event.getAmount() +
          " in " + event.getReason());
    } else if (event.getFromPlayer() == null) {
      soundManager.playSound("ladder");
      statusLabel.setText(event.getToPlayer().getName() +
          " received $" + event.getAmount() +
          " for " + event.getReason());
    } else {
      soundManager.playSound("bounce");
      statusLabel.setText(event.getFromPlayer().getName() +
          " paid $" + event.getAmount() +
          " " + event.getReason() +
          " to " + event.getToPlayer().getName());
    }
  }

  private void handlePlayerBankrupt(PlayerBankruptEvent event) {
    soundManager.playSound("freeze");
    statusLabel.setText(event.getPlayer().getName() + " is bankrupt!");
    playerScoreboard.updatePlayerMoney(event.getPlayer());

    if (event.getPlayer().equals(gameController.getGame().getCurrentPlayer())) {
      rollDiceButton.setDisable(true);
      buyPropertyButton.setDisable(true);
    }
  }

  @Override
  protected void handleGameEvent(GameEvent event) {
    monopolyController.updateGameReference();

    if (event instanceof GameStartedEvent startedEvent) {
      handleGameStarted(startedEvent);
    } else if (event instanceof DiceRolledEvent diceEvent) {
      handleDiceRolled(diceEvent);
    } else if (event instanceof PlayerMovedEvent moveEvent) {
      handlePlayerMoved(moveEvent);
    } else if (event instanceof TurnChangedEvent turnEvent) {
      handleTurnChanged(turnEvent);
    } else if (event instanceof PropertyPurchasedEvent propEvent) {
      handlePropertyPurchased(propEvent);
    } else if (event instanceof MoneyTransferEvent transferEvent) {
      handleMoneyTransfer(transferEvent);
    } else if (event instanceof PlayerBankruptEvent bankruptEvent) {
      handlePlayerBankrupt(bankruptEvent);
    }
  }
}
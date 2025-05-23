package edu.ntnu.stud.boardgame.view;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.controller.MainController;
import edu.ntnu.stud.boardgame.controller.MonopolyController;
import edu.ntnu.stud.boardgame.controller.MonopolyController.DiceRollResult;
import edu.ntnu.stud.boardgame.controller.MonopolyController.GameRestartResult;
import edu.ntnu.stud.boardgame.controller.MonopolyController.PlayerActionState;
import edu.ntnu.stud.boardgame.controller.MonopolyController.PropertyPurchaseResult;
import edu.ntnu.stud.boardgame.model.Player;
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

/**
 * The main view for the Monopoly game. Handles game board display, player controls, property
 * management, and game event visualization. Extends {@link AbstractGameView} to provide common game
 * view functionality.
 *
 * @see AbstractGameView
 * @see MonopolyGameBoard
 * @see MonopolyPlayerScoreboard
 * @see MonopolyController
 */
public class MonopolyGameView extends AbstractGameView {

  private final MonopolyController monopolyController;
  private final MonopolyGameBoard gameBoard;
  private final MonopolyPlayerScoreboard playerScoreboard;

  // UI Components
  private final Label statusLabel;
  private final Button rollDiceButton;
  private final Button buyPropertyButton;

  /**
   * Creates a new Monopoly game view.
   *
   * <p>Initializes the game board, scoreboard, and control buttons. Sets up the Monopoly-specific
   * controller.
   *
   * @param mainController The main application controller
   * @param gameController The game-specific controller
   */
  public MonopolyGameView(MainController mainController, GameController gameController) {
    super(mainController, gameController);
    this.monopolyController = new MonopolyController(gameController);

    this.gameBoard = new MonopolyGameBoard(monopolyController);
    this.playerScoreboard = new MonopolyPlayerScoreboard(monopolyController);

    this.statusLabel =
        new LabelBuilder().text("Welcome to Monopoly").styleClass("title").wrapText(true).build();

    this.rollDiceButton =
        new ButtonBuilder()
            .text("Roll Dice")
            .styleClass("action-button")
            .onClick(e -> handleDiceRollRequest())
            .disabled(true)
            .build();

    this.buyPropertyButton =
        new ButtonBuilder()
            .text("Buy Property")
            .styleClass("action-button")
            .onClick(e -> handlePropertyPurchaseRequest())
            .disabled(true)
            .build();

    initializeLayout();
  }

  private void initializeLayout() {
    VBox controlPanel = createControlPanel();

    VBox leftPanel = new VBox(20);
    leftPanel.setPadding(new Insets(10));
    leftPanel.getChildren().addAll(controlPanel, playerScoreboard);
    leftPanel.setPrefWidth(300);

    gameArea.getChildren().add(0, gameBoard);
    setLeft(leftPanel);
  }

  private VBox createControlPanel() {
    VBox controlPanel = new VBox(15);
    controlPanel.setPadding(new Insets(15));
    controlPanel.setSpacing(15);
    controlPanel.getStyleClass().add("card");

    Label titleLabel = new LabelBuilder().text("Monopoly").styleClass("text-h2").build();

    Button restartButton =
        new ButtonBuilder()
            .text("Restart Game")
            .styleClass("btn-secondary")
            .onClick(e -> handleGameRestartRequest())
            .build();

    controlPanel
        .getChildren()
        .addAll(titleLabel, statusLabel, rollDiceButton, buyPropertyButton, restartButton);

    return controlPanel;
  }

  // Pure UI event handlers - no business logic
  private void handleDiceRollRequest() {
    DiceRollResult result = monopolyController.attemptDiceRoll();

    if (!result.isSuccess()) {
      statusLabel.setText(result.getMessage());
    }
    // Success handling happens through game events
  }

  private void handlePropertyPurchaseRequest() {
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    statusLabel.setText(result.getMessage());

    if (result.isSuccess()) {
      gameBoard.refreshBoard();
      updateUiState();
    }
  }

  private void handleGameRestartRequest() {
    GameRestartResult result = monopolyController.attemptGameRestart();

    if (result.isSuccess()) {
      statusLabel.setText("Game restarted successfully");
    } else {
      statusLabel.setText("Failed to restart game");
    }
  }

  // Update UI based on current game state
  private void updateUiState() {
    PlayerActionState state = monopolyController.getCurrentPlayerActionState();

    rollDiceButton.setDisable(!state.canRoll());
    buyPropertyButton.setDisable(!state.canBuyProperty());
  }

  // Event handlers - focused on presentation only
  private void handleGameStarted(GameStartedEvent event) {
    gameBoard.clearPlayerPieces();
    gameBoard.setBoard(event.getBoard());
    playerScoreboard.updatePlayers(event.getPlayers());

    statusLabel.setText("Game Started - Roll the Dice");
    playerScoreboard.highlightCurrentPlayer(event.getCurrentPlayer());
    victoryScreen.setVisible(false);

    updateUiState();

    // Update player positions
    for (Player player : event.getPlayers()) {
      if (player.getCurrentTile() != null) {
        gameBoard.updatePlayerPosition(player, player.getCurrentTile());
      }
    }
  }

  private void handleDiceRolled(DiceRolledEvent event) {
    soundManager.playSound("dice_roll");
    statusLabel.setText(event.getCurrentPlayer().getName() + " rolled " + event.getDiceValue());
  }

  private void handlePlayerMoved(PlayerMovedEvent event) {
    soundManager.playSound("move");
    gameBoard.animatePlayerMove(event.getPlayer(), event.getFromTile(), event.getToTile());

    String tileName =
        event.getToTile().getName() != null
            ? event.getToTile().getName()
            : "Tile " + event.getToTile().getTileId();
    statusLabel.setText(event.getPlayer().getName() + " moved to " + tileName);

    updateUiState();
  }

  private void handleTurnChanged(TurnChangedEvent event) {
    Player currentPlayer = event.getCurrentPlayer();
    statusLabel.setText(currentPlayer.getName() + "'s turn");
    playerScoreboard.highlightCurrentPlayer(currentPlayer);

    updateUiState();
  }

  private void handlePropertyPurchased(PropertyPurchasedEvent event) {
    soundManager.playSound("receipt");

    String propertyName =
        event.getProperty().getName() != null ? event.getProperty().getName() : "Property";
    statusLabel.setText(
        event.getPlayer().getName() + " bought " + propertyName + " for $" + event.getPrice());

    gameBoard.refreshBoard();
    playerScoreboard.updatePlayerMoney(event.getPlayer());
    updateUiState();
  }

  private void handleMoneyTransfer(MoneyTransferEvent event) {
    // Update player money displays
    if (event.getFromPlayer() != null) {
      playerScoreboard.updatePlayerMoney(event.getFromPlayer());
    }
    if (event.getToPlayer() != null) {
      playerScoreboard.updatePlayerMoney(event.getToPlayer());
    }

    // Play appropriate sound and show message
    if (event.getToPlayer() == null) {
      soundManager.playSound("snake");
      statusLabel.setText(
          event.getFromPlayer().getName()
              + " paid $"
              + event.getAmount()
              + " in "
              + event.getReason());
    } else if (event.getFromPlayer() == null) {
      soundManager.playSound("cash_incoming");
      statusLabel.setText(
          event.getToPlayer().getName()
              + " received $"
              + event.getAmount()
              + " for "
              + event.getReason());
    } else {
      soundManager.playSound("bounce");
      statusLabel.setText(
          event.getFromPlayer().getName()
              + " paid $"
              + event.getAmount()
              + " "
              + event.getReason()
              + " to "
              + event.getToPlayer().getName());
    }

    updateUiState();
  }

  private void handlePlayerBankrupt(PlayerBankruptEvent event) {
    soundManager.playSound("freeze");
    statusLabel.setText(event.getPlayer().getName() + " is bankrupt!");
    playerScoreboard.updatePlayerMoney(event.getPlayer());

    updateUiState();
  }

  @Override
  protected void handleGameEvent(GameEvent event) {
    // Update controller's game reference
    monopolyController.updateGameReference();

    // Handle events with simple delegation
    switch (event) {
      case GameStartedEvent startedEvent -> handleGameStarted(startedEvent);
      case DiceRolledEvent diceEvent -> handleDiceRolled(diceEvent);
      case PlayerMovedEvent moveEvent -> handlePlayerMoved(moveEvent);
      case TurnChangedEvent turnEvent -> handleTurnChanged(turnEvent);
      case PropertyPurchasedEvent propEvent -> handlePropertyPurchased(propEvent);
      case MoneyTransferEvent transferEvent -> handleMoneyTransfer(transferEvent);
      case PlayerBankruptEvent bankruptEvent -> handlePlayerBankrupt(bankruptEvent);
      default -> {
        /* Ignore unknown events */ }
    }
  }
}

package edu.ntnu.stud.boardgame.snakesandladders.view;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.BoardGameObserver;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.snakesandladders.controller.ControlPanelController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * View for the control panel. Provides UI controls for game management and displays game status.
 */
public class ControlPanelView extends BorderPane implements BoardGameObserver {

  private final ControlPanelController controller;

  private Label titleLabel;
  private Label statusLabel;
  private Label currentPlayerLabel;
  private Label diceResultLabel;

  private TextField playerNameField;
  private ComboBox<String> tokenComboBox;

  private Button newGameButton;
  private Button addPlayerButton;
  private Button startGameButton;
  private Button rollDiceButton;

  /**
   * Constructs a new control panel view with the specified controller.
   *
   * @param controller The control panel controller
   */
  public ControlPanelView(ControlPanelController controller) {
    this.controller = controller;

    setupUI();

    controller.getGameBoardController().getGame().addObserver(this);
  }

  /**
   * Sets up the user interface components.
   */
  private void setupUI() {
    setPadding(new Insets(10));
    setPrefWidth(250);

    titleLabel = new Label("Snakes and Ladders");
    titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    titleLabel.setPadding(new Insets(0, 0, 10, 0));

    VBox controlsBox = new VBox(10);
    controlsBox.setAlignment(Pos.TOP_CENTER);

    Label addPlayerLabel = new Label("Add New Player");
    addPlayerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

    playerNameField = new TextField();
    playerNameField.setPromptText("Player Name");

    tokenComboBox = new ComboBox<>();
    tokenComboBox.getItems().addAll(controller.getAvailableTokens());
    tokenComboBox.setPromptText("Select Token");
    tokenComboBox.setPrefWidth(200);

    addPlayerButton = Button.builder()
        .text("Add Player")
        .styleClass("primary")
        .onAction(e -> addPlayer())
        .build();

    newGameButton = Button.builder()
        .text("New Game")
        .styleClass("primary")
        .width(200)
        .onAction(e -> createNewGame())
        .build();

    startGameButton = Button.builder()
        .text("Start Game")
        .styleClass("success")
        .width(200)
        .onAction(e -> startGame())
        .disabled(true)
        .build();

    rollDiceButton = Button.builder()
        .text("Roll Dice")
        .styleClass("primary")
        .width(200)
        .onAction(e -> rollDice())
        .disabled(true)
        .build();

    currentPlayerLabel = new Label("Current Player: None");
    diceResultLabel = new Label("Dice: ");
    statusLabel = new Label("Status: Create a new game to begin");

    controlsBox.getChildren().addAll(
        addPlayerLabel,
        playerNameField,
        tokenComboBox,
        addPlayerButton,
        new Label(""),
        newGameButton,
        startGameButton,
        rollDiceButton,
        new Label(""),
        currentPlayerLabel,
        diceResultLabel,
        statusLabel
    );

    setTop(titleLabel);
    setCenter(controlsBox);

    playerNameField.setDisable(true);
    tokenComboBox.setDisable(true);
    addPlayerButton.setDisable(true);
  }

  /**
   * Creates a new game.
   */
  private void createNewGame() {
    controller.createNewGame();

    playerNameField.setDisable(false);
    tokenComboBox.setDisable(false);
    addPlayerButton.setDisable(false);

    startGameButton.setDisable(true);
    rollDiceButton.setDisable(true);

    playerNameField.clear();
    tokenComboBox.getSelectionModel().clearSelection();

    statusLabel.setText("Status: Add players to start the game");
    currentPlayerLabel.setText("Current Player: None");
    diceResultLabel.setText("Dice: ");
  }

  /**
   * Adds a player to the game.
   */
  private void addPlayer() {
    String name = playerNameField.getText().trim();
    String token = tokenComboBox.getValue();

    if (name.isEmpty()) {
      showError("Please enter a player name");
      return;
    }

    if (token == null) {
      showError("Please select a token");
      return;
    }

    try {
      controller.addPlayer(name, token);

      playerNameField.clear();
      tokenComboBox.getSelectionModel().clearSelection();
      statusLabel.setText("Status: Player " + name + " added");

      startGameButton.setDisable(false);

    } catch (Exception e) {
      showError("Error adding player: " + e.getMessage());
    }
  }

  /**
   * Starts the game.
   */
  private void startGame() {
    try {
      controller.startGame();

      playerNameField.setDisable(true);
      tokenComboBox.setDisable(true);
      addPlayerButton.setDisable(true);

      startGameButton.setDisable(true);
      rollDiceButton.setDisable(false);

      Player currentPlayer = controller.getGameBoardController().getCurrentPlayer();
      if (currentPlayer != null) {
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
      }
      statusLabel.setText("Status: Game started");

    } catch (Exception e) {
      showError("Error starting game: " + e.getMessage());
    }
  }

  /**
   * Rolls the dice and takes a turn.
   */
  private void rollDice() {
    try {
      int result = controller.rollDiceAndTakeTurn();
      diceResultLabel.setText("Dice: " + result);

      Player currentPlayer = controller.getGameBoardController().getCurrentPlayer();
      if (currentPlayer != null) {
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
      }

      if (controller.isGameFinished()) {
        Player winner = controller.getWinner();
        if (winner != null) {
          statusLabel.setText("Status: Game over! " + winner.getName() + " wins!");
          rollDiceButton.setDisable(true);
        }
      }

    } catch (Exception e) {
      showError("Error rolling dice: " + e.getMessage());
    }
  }

  /**
   * Shows an error dialog with the specified message.
   *
   * @param message The error message
   */
  private void showError(String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @Override
  public void onGameEvent(GameEvent event) {
    switch (event.getEventType()) {
      case GAME_STARTED:
        statusLabel.setText("Status: Game started");
        break;

      case PLAYER_MOVED:
        Player player = (Player) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: " + player.getName() + " moved");
        }
        break;

      case SNAKE_ENCOUNTERED:
        player = (Player) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: " + player.getName() + " went down a snake!");
        }
        break;

      case LADDER_CLIMBED:
        player = (Player) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: " + player.getName() + " climbed a ladder!");
        }
        break;

      case PLAYER_WON:
        player = (Player) event.getData("player");
        if (player != null) {
          statusLabel.setText("Status: Game over! " + player.getName() + " wins!");
          rollDiceButton.setDisable(true);
        }
        break;

      case DICE_ROLLED:
        Integer result = (Integer) event.getData("result");
        if (result != null) {
          diceResultLabel.setText("Dice: " + result);
        }
        break;

      case TURN_CHANGED:
        player = (Player) event.getData("player");
        if (player != null) {
          currentPlayerLabel.setText("Current Player: " + player.getName());
        }
        break;

      default:
        break;
    }
  }
}
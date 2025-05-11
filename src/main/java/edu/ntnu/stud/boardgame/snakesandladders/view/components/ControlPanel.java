package edu.ntnu.stud.boardgame.snakesandladders.view.components;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.DiceRolledEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameRestartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameStartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerWonEvent;
import edu.ntnu.stud.boardgame.core.observer.events.TurnChangedEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.core.view.ui.Panel;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ControlPanel extends GameComponent<Panel> {

  private static final Logger LOGGER = Logger.getLogger(ControlPanel.class.getName());

  private final SlGameController controller;
  private Label statusLabel;
  private Label currentPlayerLabel;
  private Label diceResultLabel;
  private Button newGameButton;
  private Button startGameButton;
  private Button rollDiceButton;

  public ControlPanel(SlGameController controller) {
    super(controller, new Panel());
    this.controller = controller;

    setupComponents();
  }

  private void setupComponents() {
    // Title label
    Label titleLabel = Label.builder().text("Snakes and Ladders").build();

    // Status information
    statusLabel = Label.builder().text("Status: Create a new game to begin").wrapText(true).build();
    currentPlayerLabel = Label.builder().text("Current Player: None").build();
    diceResultLabel = Label.builder().text("Dice: ").build();

    // Buttons
    newGameButton = newGameButton();
    startGameButton = startGameButton();
    rollDiceButton = createRollButton();

    startGameButton.setDisable(true);
    rollDiceButton.setDisable(true);

    getNode().getChildren().addAll(
        titleLabel,
        newGameButton,
        startGameButton,
        rollDiceButton,
        currentPlayerLabel,
        diceResultLabel,
        statusLabel
    );

    getNode().setSpacing(10);
  }

  private Button newGameButton() {
    Button newGameButton = Button.builder().text("New Game").styleClass("primary").width(200)
        .build();
    newGameButton.setOnAction(event -> newGameButtonHandler());
    return newGameButton;
  }

  private Button startGameButton() {
    Button startGameButton = Button.builder().text("Start Game").styleClass("success").width(200)
        .build();
    startGameButton.setOnAction(event -> {
      if (startGameButton.getText().equals("Start Game")) {
        startGameButtonHandler();
      } else {
        restartGameButtonHandler();
      }
    });
    return startGameButton;
  }

  private Button createRollButton() {
    Button rollButton = Button.builder().text("Roll Dice").styleClass("primary").width(200).build();
    rollButton.setOnAction(event -> rollDiceButtonHandler());
    return rollButton;
  }

  private void newGameButtonHandler() {
    try {
      controller.createNewGame();
      startGameButton.setDisable(true);
      rollDiceButton.setDisable(true);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error creating new game", e);
      showAlert("Error creating new game: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void startGameButtonHandler() {
    try {
      controller.startGame();
      // Change button to "Restart Game" after starting
      startGameButton.setText("Restart Game");
      startGameButton.setDisable(false);
      startGameButton.getStyleClass().setAll("button", "danger");
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error starting game", e);
      showAlert("Error starting game: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void restartGameButtonHandler() {
    try {
      controller.restartGame();
      statusLabel.setText("Status: Game restarted");
      rollDiceButton.setDisable(false);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error restarting game", e);
      showAlert("Error restarting game: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void rollDiceButtonHandler() {
    try {
      controller.rollDiceAndTakeTurn();
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error rolling dice", e);
      showAlert("Error rolling dice: " + e.getMessage(), AlertType.ERROR);
    }
  }

  private void showAlert(String message, AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(alertType.toString());
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @Override
  public void onGameEvent(GameEvent e) {
    switch (e) {
      case GameCreatedEvent event -> {
        currentPlayerLabel.setText("Current Player: None");
        diceResultLabel.setText("Dice: ");
        statusLabel.setText("Status: Add players to start the game");
        startGameButton.setText("Start Game");
        startGameButton.getStyleClass().setAll("button", "success");
        startGameButton.setDisable(true);
        rollDiceButton.setDisable(true);
      }
      case GameResetEvent event -> {
        currentPlayerLabel.setText("Current Player: None");
        diceResultLabel.setText("Dice: ");
        statusLabel.setText("Status: Add players to start the game");
        startGameButton.setText("Start Game");
        startGameButton.getStyleClass().setAll("button", "success");
        rollDiceButton.setDisable(true);
      }
      case GameStartedEvent ignored -> {
        statusLabel.setText("Status: Game started");
        rollDiceButton.setDisable(false);
        startGameButton.setText("Restart Game");
        startGameButton.getStyleClass().setAll("button", "danger");
        startGameButton.setDisable(false);
      }
      case GameRestartedEvent event -> {
        currentPlayerLabel.setText("Current Player: None");
        diceResultLabel.setText("Dice: ");
        statusLabel.setText("Status: Game restarted");
        startGameButton.setText("Restart Game");
        startGameButton.getStyleClass().setAll("button", "danger");
        rollDiceButton.setDisable(false);
      }
      case TurnChangedEvent event -> {
        SlPlayer player = (SlPlayer) event.getCurrentPlayer();
        if (player != null) {
          currentPlayerLabel.setText("Current Player: " + player.getName());
        }
      }
      case DiceRolledEvent event -> {
        int result = event.getValue();
        diceResultLabel.setText("Dice: " + result);
      }
      case PlayerAddedEvent ignored -> {
        startGameButton.setDisable(false);
        statusLabel.setText("Status: Player added");
      }
      case PlayerMovedEvent event -> {

        SlPlayer player = (SlPlayer) event.getPlayer();
        statusLabel.setText("Status: " + player.getName() + " moved");
      }
      case PlayerWonEvent event -> {
        SlPlayer player = (SlPlayer) event.getWinner();
        statusLabel.setText("Status: Game over! " + player.getName() + " wins!");
        rollDiceButton.setDisable(true);
      }
      default -> { /* No action needed */}
    }
  }
}
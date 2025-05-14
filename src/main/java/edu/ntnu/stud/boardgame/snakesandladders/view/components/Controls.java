package edu.ntnu.stud.boardgame.snakesandladders.view.components;

import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import edu.ntnu.stud.boardgame.core.observer.events.DiceRolledEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameCreatedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameEndedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameResetEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameRestartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.GameStartedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerAddedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.PlayerMovedEvent;
import edu.ntnu.stud.boardgame.core.observer.events.TurnChangedEvent;
import edu.ntnu.stud.boardgame.core.view.GameComponent;
import edu.ntnu.stud.boardgame.core.view.component.ErrorDialog;
import edu.ntnu.stud.boardgame.core.view.ui.Button;
import edu.ntnu.stud.boardgame.core.view.ui.Label;
import edu.ntnu.stud.boardgame.snakesandladders.controller.SlGameController;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Controls extends GameComponent<VBox> {

  private final Label titleLabel;
  private final Label statusLabel;
  private final Label currentPlayerLabel;
  private final Label diceResultLabel;

  private final Button newGameButton;
  private final Button startGameButton;
  private final Button rollDiceButton;

  private int lastDiceResult = 0;

  public Controls(SlGameController controller, ErrorDialog errorDialog) {
    super(controller, new VBox(10), errorDialog);

    // Create title and status labels
    titleLabel = Label.builder().text("Snakes and Ladders").build();
    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

    statusLabel = Label.builder().text("Add players to start the game").build();
    statusLabel.setWrapText(true);

    currentPlayerLabel = Label.builder().text("Current Player: None").build();
    diceResultLabel = Label.builder().text("Dice: ").build();

    // Create control buttons
    newGameButton = Button.builder().text("New Game").build();
    newGameButton.setMaxWidth(Double.MAX_VALUE);
    newGameButton.setOnAction(e -> handleNewGame());

    startGameButton = Button.builder().text("Start Game").build();
    startGameButton.setMaxWidth(Double.MAX_VALUE);
    startGameButton.setDisable(true);
    startGameButton.setOnAction(e -> handleStartGame());

    rollDiceButton = Button.builder().text("Roll Dice").build();
    rollDiceButton.setMaxWidth(Double.MAX_VALUE);
    rollDiceButton.setDisable(true);
    rollDiceButton.setOnAction(e -> handleRollDice());

    // Create dice result display
    HBox diceDisplay = new HBox(10);
    diceDisplay.setAlignment(Pos.CENTER);
    diceDisplay.setPadding(new Insets(10));
    diceDisplay.setBackground(new Background(
        new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5), Insets.EMPTY)
    ));

    Label diceLabel = Label.builder().text("🎲").build();
    diceLabel.setFont(Font.font("System", 24));

    Label diceValue = Label.builder().text(String.valueOf(lastDiceResult)).build();
    diceValue.setFont(Font.font("System", FontWeight.BOLD, 24));

    diceDisplay.getChildren().addAll(diceLabel, diceValue);

    // Set up layout
    getNode().setPadding(new Insets(10));
    getNode().getChildren().addAll(
        titleLabel,
        new Separator(),
        statusLabel,
        currentPlayerLabel,
        newGameButton,
        startGameButton,
        rollDiceButton,
        diceDisplay
    );
  }

  private void handleNewGame() {
    boolean success = ((SlGameController) controller).resetGame();
    if (success) {
      statusLabel.setText("Game reset. Add players to start a new game.");
      startGameButton.setDisable(true);
      rollDiceButton.setDisable(true);
      currentPlayerLabel.setText("Current Player: None");
    }
  }

  private void handleStartGame() {
    // If button says "Start Game", start a new game
    if (startGameButton.getText().equals("Start Game")) {
      boolean success = ((SlGameController) controller).startGame();
      if (success) {
        statusLabel.setText("Game started. Roll the dice to begin.");
        startGameButton.setText("Restart Game");
        rollDiceButton.setDisable(false);
      }
    }
    // If button says "Restart Game", restart the current game
    else {
      boolean success = ((SlGameController) controller).restartGame();
      if (success) {
        statusLabel.setText("Game restarted. Roll the dice to begin.");
        rollDiceButton.setDisable(false);
      }
    }
  }

  private void handleRollDice() {
    boolean success = ((SlGameController) controller).playCurrentTurn();
  }

  @Override
  public void init() {
    statusLabel.setText("Add players to start the game");
    currentPlayerLabel.setText("Current Player: None");
    startGameButton.setText("Start Game");
    startGameButton.setDisable(true);
    rollDiceButton.setDisable(true);
  }

  @Override
  public void onGameEvent(GameEvent event) {
    if (event instanceof GameCreatedEvent) {
      statusLabel.setText("Add players to start the game");
      currentPlayerLabel.setText("Current Player: None");
      startGameButton.setText("Start Game");
      startGameButton.setDisable(true);
      rollDiceButton.setDisable(true);

    } else if (event instanceof GameResetEvent) {
      statusLabel.setText("Game reset. Add players to start a new game.");
      currentPlayerLabel.setText("Current Player: None");
      startGameButton.setText("Start Game");
      startGameButton.setDisable(true);
      rollDiceButton.setDisable(true);

    } else if (event instanceof GameStartedEvent) {
      statusLabel.setText("Game started. Roll the dice to begin.");
      startGameButton.setText("Restart Game");
      startGameButton.setDisable(false);
      rollDiceButton.setDisable(false);

    } else if (event instanceof GameRestartedEvent) {
      statusLabel.setText("Game restarted. Roll the dice to begin.");
      rollDiceButton.setDisable(false);

    } else if (event instanceof PlayerAddedEvent) {
      startGameButton.setDisable(false);
      statusLabel.setText("Player added. You can add more or start the game.");

    } else if (event instanceof TurnChangedEvent turnChangedEvent) {
      SlPlayer player = (SlPlayer) turnChangedEvent.getCurrentPlayer();
      if (player != null) {
        currentPlayerLabel.setText("Current Player: " + player.getName());
      }

    } else if (event instanceof DiceRolledEvent diceRolledEvent) {
      lastDiceResult = diceRolledEvent.getValue();
      diceResultLabel.setText("Dice: " + lastDiceResult);
      // Update dice display
      for (javafx.scene.Node node : getNode().getChildren()) {
        if (node instanceof HBox hbox && hbox.getChildren().size() > 1 &&
            hbox.getChildren().get(0) instanceof Label label && label.getText().equals("🎲")) {
          Label valueLabel = (Label) hbox.getChildren().get(1);
          valueLabel.setText(String.valueOf(lastDiceResult));
        }
      }

    } else if (event instanceof PlayerMovedEvent playerMovedEvent) {
      SlPlayer player = (SlPlayer) playerMovedEvent.getPlayer();
      statusLabel.setText(player.getName() + " moved to tile " +
          playerMovedEvent.getToTile().getTileId());

    } else if (event instanceof GameEndedEvent gameEndedEvent) {
      SlPlayer winner = (SlPlayer) gameEndedEvent.getWinner();
      statusLabel.setText("Game over! " + winner.getName() + " wins!");
      rollDiceButton.setDisable(true);
    }
  }
}
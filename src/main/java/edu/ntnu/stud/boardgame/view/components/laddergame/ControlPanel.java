package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ControlPanel extends VBox {

  private final GameController gameController;
  private final Label titleLabel;
  private final Label playerTurnLabel;
  private final Label diceValueLabel;
  private final Button rollDiceButton;
  private final Button restartButton;

  public ControlPanel(GameController gameController) {
    this.gameController = gameController;

    setPadding(new Insets(15));
    setSpacing(15);
    getStyleClass().add("control-panel");

    titleLabel = new LabelBuilder()
        .text("Snakes and Ladders")
        .styleClass("panel-title")
        .build();

    playerTurnLabel = new LabelBuilder()
        .text("Waiting to start...")
        .styleClass("player-turn")
        .build();

    diceValueLabel = new LabelBuilder()
        .text("Dice: -")
        .styleClass("dice-value")
        .build();

    rollDiceButton = new ButtonBuilder()
        .text("Roll Dice")
        .styleClass("action-button")
        .onClick(e -> rollDice())
        .disabled(true)
        .build();

    restartButton = new ButtonBuilder()
        .text("Restart Game")
        .styleClass("secondary-button")
        .onClick(e -> restartGame())
        .build();

    getChildren().addAll(
        titleLabel,
        playerTurnLabel,
        diceValueLabel,
        rollDiceButton,
        restartButton
    );

    setAlignment(Pos.TOP_CENTER);
  }

  public void updateDiceValue(int value) {
    diceValueLabel.setText("Dice: " + value);
  }

  public void updateCurrentPlayer(Player player) {
    if (player != null) {
      playerTurnLabel.setText("Current Turn: " + player.getName());
      rollDiceButton.setDisable(false);
    } else {
      playerTurnLabel.setText("Waiting to start...");
      rollDiceButton.setDisable(true);
    }
  }

  private void rollDice() {
    try {
      gameController.playTurn();
    } catch (Exception e) {
      System.err.println("Error rolling dice: " + e.getMessage());
    }
  }

  private void restartGame() {
    try {
      gameController.startGame();
      diceValueLabel.setText("Dice: -");
    } catch (Exception e) {
      System.err.println("Error restarting game: " + e.getMessage());
    }
  }
}
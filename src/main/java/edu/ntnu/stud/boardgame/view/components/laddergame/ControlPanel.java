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

/**
 * A control panel component for the Snakes and Ladders game. Provides game controls including dice
 * rolling and game restart functionality. Extends {@link VBox} to arrange controls in a vertical
 * layout.
 *
 * @see GameController
 * @see VBox
 */
public class ControlPanel extends VBox {

  private final GameController gameController;
  private final Label playerTurnLabel;
  private final Label diceValueLabel;
  private final Button rollDiceButton;

  /**
   * Creates a new control panel with game controls.
   *
   * @param gameController the {@link GameController} managing game logic
   */
  public ControlPanel(GameController gameController) {
    this.gameController = gameController;

    setPadding(new Insets(15));
    setSpacing(15);
    getStyleClass().add("card");

    playerTurnLabel =
        new LabelBuilder().text("Waiting to start...").styleClass("text-body-bold").build();

    diceValueLabel = new LabelBuilder().text("Dice: -").styleClass("text-accent").build();

    rollDiceButton =
        new ButtonBuilder()
            .text("Roll Dice")
            .styleClass("btn-primary")
            .onClick(e -> rollDice())
            .disabled(true)
            .build();

    Button restartButton =
        new ButtonBuilder()
            .text("Restart Game")
            .styleClass("btn-secondary")
            .onClick(e -> restartGame())
            .build();

    Label titleLabel = new LabelBuilder().text("Snakes and Ladders").styleClass("text-h2").build();
    getChildren()
        .addAll(titleLabel, playerTurnLabel, diceValueLabel, rollDiceButton, restartButton);

    setAlignment(Pos.TOP_CENTER);
  }

  /**
   * Updates the displayed dice value.
   *
   * @param value the new dice value to display
   */
  public void updateDiceValue(int value) {
    diceValueLabel.setText("Dice: " + value);
  }

  /**
   * Enables or disables the dice roll button.
   *
   * @param disabled {@code true} to disable the button, {@code false} to enable it
   */
  public void setDiceDisabled(boolean disabled) {
    rollDiceButton.setDisable(disabled);
  }

  /**
   * Updates the display to show the current player's turn.
   *
   * @param player the {@link Player} whose turn it is, or {@code null} if game hasn't started
   */
  public void updateCurrentPlayer(Player player) {
    if (player != null) {
      playerTurnLabel.setText("Current Turn: " + player.getName());
      rollDiceButton.setDisable(false);
    } else {
      playerTurnLabel.setText("Waiting to start...");
      rollDiceButton.setDisable(true);
    }
  }

  /**
   * Handles dice roll action by delegating to the {@link GameController}. Errors during roll are
   * logged to stderr.
   */
  private void rollDice() {
    try {
      gameController.playTurn();
    } catch (Exception e) {
      System.err.println("Error rolling dice: " + e.getMessage());
    }
  }

  /**
   * Restarts the game by delegating to the {@link GameController}. Resets the dice display and logs
   * any errors to stderr.
   */
  private void restartGame() {
    try {
      gameController.startGame();
      diceValueLabel.setText("Dice: -");
    } catch (Exception e) {
      System.err.println("Error restarting game: " + e.getMessage());
    }
  }
}

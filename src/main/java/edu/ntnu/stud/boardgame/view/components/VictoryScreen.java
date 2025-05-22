package edu.ntnu.stud.boardgame.view.components;

import edu.ntnu.stud.boardgame.controller.GameController;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * A victory screen overlay that appears when a game is won.
 * Features animated transitions, winner display, and a play again option.
 * Extends {@link StackPane} to center content and provide overlay
 * functionality.
 *
 * @see GameController
 * @see StackPane
 */
public class VictoryScreen extends StackPane {

  private final Label victoryLabel;
  private final Label winnerLabel;

  /**
   * <p>
   * Creates a new victory screen with animated elements and play again button.
   * </p>
   *
   * @param gameController The controller to handle game restart
   */
  public VictoryScreen(GameController gameController) {

    setAlignment(Pos.CENTER);

    setPickOnBounds(false);
    setMaxWidth(500);
    setMaxHeight(400);
    setPrefWidth(400);
    setPrefHeight(300);

    Rectangle background = new Rectangle();
    background.setFill(Color.rgb(0, 0, 0, 0.7));
    background.setArcWidth(20);
    background.setArcHeight(20);
    background.widthProperty().bind(widthProperty().subtract(40));
    background.heightProperty().bind(heightProperty().subtract(40));

    VBox contentBox = new VBox(20);
    contentBox.setAlignment(Pos.CENTER);
    contentBox.setPadding(new Insets(30));
    contentBox.setMaxWidth(400);
    contentBox.setMinWidth(300);

    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.rgb(0, 0, 0, 0.5));
    shadow.setRadius(15);
    shadow.setOffsetX(3);
    shadow.setOffsetY(3);
    background.setEffect(shadow);

    victoryLabel = new LabelBuilder().text("VICTORY!").styleClass("victory-title").build();
    victoryLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: gold;");

    winnerLabel = new LabelBuilder().text("").styleClass("winner-label").build();
    winnerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

    Label messageLabel = new LabelBuilder().text("Congratulations on winning the game!")
        .wrapText(true)
        .textAlignment(TextAlignment.CENTER).styleClass("victory-message").build();
    messageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

    Button playAgainButton = new ButtonBuilder().text("Play Again").styleClass("victory-button")
        .onClick(e -> gameController.startGame()).build();
    playAgainButton.setStyle(
        "-fx-font-size: 16px; -fx-padding: 10 20 10 20; -fx-background-color: gold; "
            + "-fx-text-fill: black;");

    contentBox.getChildren().addAll(victoryLabel, winnerLabel, messageLabel, playAgainButton);

    getChildren().addAll(background, contentBox);

    getStyleClass().add("victory-screen");

    setVisible(false);
  }

  /**
   * <p>
   * Displays the victory screen with the winning player's name.
   * </p>
   * <p>
   * Triggers victory animations and sound effects.
   * </p>
   *
   * @param winner The winning player, or null for a game over state
   */
  public void showVictory(Player winner) {
    if (winner != null) {
      winnerLabel.setText(winner.getName() + " wins!");
    } else {
      winnerLabel.setText("Game Over!");
    }

    setVisible(true);
    playVictoryAnimation();
  }

  /**
   * <p>
   * Plays the victory screen entrance and celebration animations.
   * </p>
   */
  private void playVictoryAnimation() {
    setOpacity(0);
    setScaleX(0.8);
    setScaleY(0.8);

    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), this);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.5), this);
    scaleUp.setFromX(0.8);
    scaleUp.setFromY(0.8);
    scaleUp.setToX(1);
    scaleUp.setToY(1);

    fadeIn.play();
    scaleUp.play();

    ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.5), victoryLabel);
    pulse.setFromX(1);
    pulse.setFromY(1);
    pulse.setToX(1.2);
    pulse.setToY(1.2);
    pulse.setCycleCount(6);
    pulse.setAutoReverse(true);
    pulse.play();
  }
}
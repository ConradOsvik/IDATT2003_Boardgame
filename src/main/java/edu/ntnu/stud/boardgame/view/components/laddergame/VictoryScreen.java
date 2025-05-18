package edu.ntnu.stud.boardgame.view.components.laddergame;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class VictoryScreen extends VBox {

  private final Label victoryLabel;
  private final Label winnerLabel;
  private final Label messageLabel;

  public VictoryScreen() {
    setAlignment(Pos.CENTER);
    setPadding(new Insets(30));
    setSpacing(20);
    setMaxSize(400, 300);

    Rectangle background = new Rectangle();
    background.setFill(Color.rgb(0, 0, 0, 0.7));
    background.setArcWidth(20);
    background.setArcHeight(20);
    background.widthProperty().bind(widthProperty());
    background.heightProperty().bind(heightProperty());

    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.rgb(0, 0, 0, 0.5));
    shadow.setRadius(10);
    setEffect(shadow);

    victoryLabel = new LabelBuilder()
        .text("VICTORY!")
        .styleClass("victory-title")
        .build();

    winnerLabel = new LabelBuilder()
        .text("")
        .styleClass("winner-label")
        .build();

    messageLabel = new LabelBuilder()
        .text("Congratulations on winning the game!")
        .wrapText(true)
        .textAlignment(TextAlignment.CENTER)
        .styleClass("victory-message")
        .build();

    Button playAgainButton = new ButtonBuilder()
        .text("Play Again")
        .styleClass("victory-button")
        .build();

    getStyleClass().add("victory-screen");

    getChildren().addAll(victoryLabel, winnerLabel, messageLabel, playAgainButton);

    getChildren().add(0, background);
  }

  public void showVictory(Player winner) {
    if (winner != null) {
      winnerLabel.setText(winner.getName() + " wins!");
    } else {
      winnerLabel.setText("Game Over!");
    }

    playVictoryAnimation();
  }

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

    Timeline pulseAnimation = new Timeline(
        new KeyFrame(Duration.ZERO, new KeyValue(victoryLabel.scaleXProperty(), 1)),
        new KeyFrame(Duration.ZERO, new KeyValue(victoryLabel.scaleYProperty(), 1)),
        new KeyFrame(Duration.seconds(0.5), new KeyValue(victoryLabel.scaleXProperty(), 1.2)),
        new KeyFrame(Duration.seconds(0.5), new KeyValue(victoryLabel.scaleYProperty(), 1.2)),
        new KeyFrame(Duration.seconds(1), new KeyValue(victoryLabel.scaleXProperty(), 1))
    );
    pulseAnimation.setCycleCount(3);
    pulseAnimation.play();
  }
}
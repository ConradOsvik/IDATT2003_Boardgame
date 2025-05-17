package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ButtonBuilder {

  private final Button button;

  public ButtonBuilder() {
    this.button = new Button();
  }

  public ButtonBuilder text(String text) {
    button.setText(text);
    return this;
  }

  public ButtonBuilder styleClass(String... styleClasses) {
    button.getStyleClass().addAll(styleClasses);
    return this;
  }

  public ButtonBuilder onClick(EventHandler<ActionEvent> handler) {
    button.setOnAction(handler);
    return this;
  }

  public ButtonBuilder width(double width) {
    button.setPrefWidth(width);
    return this;
  }

  public ButtonBuilder height(double height) {
    button.setPrefHeight(height);
    return this;
  }

  public ButtonBuilder disabled(boolean disabled) {
    button.setDisable(disabled);
    return this;
  }

  public Button build() {
    return button;
  }
}
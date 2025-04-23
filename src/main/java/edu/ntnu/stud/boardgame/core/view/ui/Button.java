package edu.ntnu.stud.boardgame.core.view.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Button extends javafx.scene.control.Button {
  private String text;
  private String id;
  private String styleClass;
  private String style;
  private double width;
  private double height;
  private boolean disable;
  private EventHandler<ActionEvent> onAction;

  public Button(String text) {
    super(text);
    initialize();
  }

  public Button() {
    super();
    initialize();
  }

  private void initialize() {
    getStyleClass().add("button");
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private String text;
    private String id;
    private String styleClass;
    private String style;
    private double width;
    private double height;
    private boolean disable;
    private EventHandler<ActionEvent> onAction;

    private Builder() {
      this.text = "";
      this.disable = false;
      this.width = -1;
      this.height = -1;
    }

    public Builder text(String text) {
      this.text = text;
      return this;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder styleClass(String styleClass) {
      this.styleClass = styleClass;
      return this;
    }

    public Builder style(String style) {
      this.style = style;
      return this;
    }

    public Builder width(double width) {
      this.width = width;
      return this;
    }

    public Builder height(double height) {
      this.height = height;
      return this;
    }

    public Builder disabled(boolean disable) {
      this.disable = disable;
      return this;
    }

    public Builder onAction(EventHandler<ActionEvent> handler) {
      this.onAction = handler;
      return this;
    }

    public Button build() {
      Button button = new Button(text);

      if (id != null) {
        button.setId(id);
      }

      if (styleClass != null) {
        button.getStyleClass().add(styleClass);
      }

      if (style != null) {
        button.setStyle(style);
      }

      if (width > 0) {
        button.setPrefWidth(width);
      }

      if (height > 0) {
        button.setPrefHeight(height);
      }

      if (onAction != null) {
        button.setOnAction(onAction);
      }

      button.setDisable(disable);

      return button;
    }
  }
}

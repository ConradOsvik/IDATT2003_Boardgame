package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.scene.control.TextField;

public class TextFieldBuilder {

  private final TextField textField;

  public TextFieldBuilder() {
    this.textField = new TextField();
  }

  public TextFieldBuilder promptText(String text) {
    textField.setPromptText(text);
    return this;
  }

  public TextFieldBuilder text(String text) {
    textField.setText(text);
    return this;
  }

  public TextFieldBuilder styleClass(String... styleClasses) {
    textField.getStyleClass().addAll(styleClasses);
    return this;
  }

  public TextFieldBuilder width(double width) {
    textField.setPrefWidth(width);
    return this;
  }

  public TextFieldBuilder editable(boolean editable) {
    textField.setEditable(editable);
    return this;
  }

  public TextField build() {
    return textField;
  }
}
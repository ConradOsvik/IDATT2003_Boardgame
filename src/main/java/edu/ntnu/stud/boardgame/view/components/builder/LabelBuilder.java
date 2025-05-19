package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class LabelBuilder {

  private final Label label;

  public LabelBuilder() {
    this.label = new Label();
  }

  public LabelBuilder text(String text) {
    label.setText(text);
    return this;
  }

  public LabelBuilder styleClass(String... styleClasses) {
    label.getStyleClass().addAll(styleClasses);
    return this;
  }

  public LabelBuilder textAlignment(TextAlignment alignment) {
    label.setTextAlignment(alignment);
    return this;
  }

  public LabelBuilder wrapText(boolean wrap) {
    label.setWrapText(wrap);
    return this;
  }

  public Label build() {
    return label;
  }
}
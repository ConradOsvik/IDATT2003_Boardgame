package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Slider;

public class SliderBuilder {

  private final Slider slider;

  public SliderBuilder() {
    this.slider = new Slider();
  }

  public SliderBuilder(double min, double max, double value) {
    this.slider = new Slider(min, max, value);
  }

  public SliderBuilder min(double min) {
    slider.setMin(min);
    return this;
  }

  public SliderBuilder max(double max) {
    slider.setMax(max);
    return this;
  }

  public SliderBuilder value(double value) {
    slider.setValue(value);
    return this;
  }

  public SliderBuilder majorTickUnit(double unit) {
    slider.setMajorTickUnit(unit);
    return this;
  }

  public SliderBuilder minorTickCount(int count) {
    slider.setMinorTickCount(count);
    return this;
  }

  public SliderBuilder showTickMarks(boolean show) {
    slider.setShowTickMarks(show);
    return this;
  }

  public SliderBuilder showTickLabels(boolean show) {
    slider.setShowTickLabels(show);
    return this;
  }

  public SliderBuilder disable(boolean disable) {
    slider.setDisable(disable);
    return this;
  }

  public SliderBuilder snapToTicks(boolean snap) {
    slider.setSnapToTicks(snap);
    return this;
  }

  public SliderBuilder prefWidth(double width) {
    slider.setPrefWidth(width);
    return this;
  }

  public SliderBuilder styleClass(String... styleClasses) {
    slider.getStyleClass().addAll(styleClasses);
    return this;
  }

  public SliderBuilder onChange(ChangeListener<Number> listener) {
    slider.valueProperty().addListener(listener);
    return this;
  }

  public Slider build() {
    return slider;
  }
}
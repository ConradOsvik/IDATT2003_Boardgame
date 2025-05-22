package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Slider;

/**
 * Builder class for creating and configuring JavaFX {@link Slider} instances. Provides a fluent
 * interface for setting various slider properties.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * Slider slider = new SliderBuilder(0, 100, 50)
 *     .showTickMarks(true)
 *     .showTickLabels(true)
 *     .majorTickUnit(10)
 *     .minorTickCount(5)
 *     .build();
 * }</pre>
 *
 * @see Slider
 * @see javafx.scene.control.Control
 */
public class SliderBuilder {

  private final Slider slider;

  /**
   * Creates a new SliderBuilder instance with a default {@link Slider}.
   *
   * <p>The slider is initialized with default JavaFX properties and can be customized using the
   * builder methods.
   */
  public SliderBuilder() {
    this.slider = new Slider();
  }

  /**
   * Creates a new SliderBuilder instance with a {@link Slider} initialized with specific values.
   *
   * <p>This constructor allows setting the initial range and value of the slider.
   *
   * @param min the minimum value of the slider
   * @param max the maximum value of the slider
   * @param value the initial value of the slider
   * @see Slider#Slider(double, double, double)
   */
  public SliderBuilder(double min, double max, double value) {
    this.slider = new Slider(min, max, value);
  }

  /**
   * Sets the minimum value of the slider.
   *
   * <p>This defines the lower bound of the slider's range.
   *
   * @param min the minimum value
   * @return this builder instance for method chaining
   * @see Slider#setMin(double)
   */
  public SliderBuilder min(double min) {
    slider.setMin(min);
    return this;
  }

  /**
   * Sets the maximum value of the slider.
   *
   * <p>This defines the upper bound of the slider's range.
   *
   * @param max the maximum value
   * @return this builder instance for method chaining
   * @see Slider#setMax(double)
   */
  public SliderBuilder max(double max) {
    slider.setMax(max);
    return this;
  }

  /**
   * Sets the current value of the slider.
   *
   * <p>The value must be within the slider's min and max range.
   *
   * @param value the current value
   * @return this builder instance for method chaining
   * @see Slider#setValue(double)
   */
  public SliderBuilder value(double value) {
    slider.setValue(value);
    return this;
  }

  /**
   * Sets the unit distance between major tick marks.
   *
   * <p>Major tick marks are the primary divisions on the slider's track.
   *
   * @param unit the distance between major ticks
   * @return this builder instance for method chaining
   * @see Slider#setMajorTickUnit(double)
   */
  public SliderBuilder majorTickUnit(double unit) {
    slider.setMajorTickUnit(unit);
    return this;
  }

  /**
   * Sets the number of minor tick marks between major tick marks.
   *
   * <p>Minor tick marks create smaller divisions between major tick marks.
   *
   * @param count the number of minor ticks
   * @return this builder instance for method chaining
   * @see Slider#setMinorTickCount(int)
   */
  public SliderBuilder minorTickCount(int count) {
    slider.setMinorTickCount(count);
    return this;
  }

  /**
   * Sets whether tick marks should be displayed.
   *
   * <p>Tick marks provide visual indicators of values along the slider's track.
   *
   * @param show true to show tick marks, false to hide them
   * @return this builder instance for method chaining
   * @see Slider#setShowTickMarks(boolean)
   */
  public SliderBuilder showTickMarks(boolean show) {
    slider.setShowTickMarks(show);
    return this;
  }

  /**
   * Sets whether tick labels should be displayed.
   *
   * <p>Tick labels show numeric values at major tick mark positions.
   *
   * @param show true to show tick labels, false to hide them
   * @return this builder instance for method chaining
   * @see Slider#setShowTickLabels(boolean)
   */
  public SliderBuilder showTickLabels(boolean show) {
    slider.setShowTickLabels(show);
    return this;
  }

  /**
   * Sets whether the slider is disabled.
   *
   * <p>A disabled slider cannot be interacted with and appears grayed out.
   *
   * @param disable true to disable the slider, false to enable it
   * @return this builder instance for method chaining
   * @see javafx.scene.Node#setDisable(boolean)
   */
  public SliderBuilder disable(boolean disable) {
    slider.setDisable(disable);
    return this;
  }

  /**
   * Sets whether the slider's value should snap to the nearest tick mark.
   *
   * <p>When enabled, the slider's thumb will snap to the closest tick mark position.
   *
   * @param snap true to enable snapping to ticks, false to disable it
   * @return this builder instance for method chaining
   * @see Slider#setSnapToTicks(boolean)
   */
  public SliderBuilder snapToTicks(boolean snap) {
    slider.setSnapToTicks(snap);
    return this;
  }

  /**
   * Sets the preferred width of the slider.
   *
   * <p>The actual width may vary based on the layout constraints.
   *
   * @param width the preferred width in pixels
   * @return this builder instance for method chaining
   * @see javafx.scene.layout.Region#setPrefWidth(double)
   */
  public SliderBuilder prefWidth(double width) {
    slider.setPrefWidth(width);
    return this;
  }

  /**
   * Adds style classes to the slider.
   *
   * <p>These classes should be defined in your CSS stylesheet.
   *
   * @param styleClasses one or more CSS style classes to apply
   * @return this builder instance for method chaining
   * @see javafx.scene.Node#getStyleClass()
   */
  public SliderBuilder styleClass(String... styleClasses) {
    slider.getStyleClass().addAll(styleClasses);
    return this;
  }

  /**
   * Sets a listener for value changes of the slider.
   *
   * <p>The listener will be called whenever the slider's value changes.
   *
   * @param listener the change listener to add
   * @return this builder instance for method chaining
   * @see javafx.beans.value.ObservableValue#addListener(ChangeListener)
   */
  public SliderBuilder onChange(ChangeListener<Number> listener) {
    slider.valueProperty().addListener(listener);
    return this;
  }

  /**
   * Builds and returns the configured {@link Slider} instance.
   *
   * <p>After this call, the slider is ready to be added to a scene graph.
   *
   * @return the configured JavaFX {@link Slider}
   */
  public Slider build() {
    return slider;
  }
}

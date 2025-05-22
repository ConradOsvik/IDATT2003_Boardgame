package edu.ntnu.stud.boardgame.view.components.builder;

import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

/**
 * Builder class for creating and configuring JavaFX {@link Label} instances. Provides a fluent
 * interface for setting various label properties.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * Label label = new LabelBuilder()
 *     .text("Hello World")
 *     .textAlignment(TextAlignment.CENTER)
 *     .wrapText(true)
 *     .build();
 * }</pre>
 *
 * @see Label
 * @see javafx.scene.control.Control
 */
public class LabelBuilder {

  private final Label label;

  /**
   * Creates a new LabelBuilder instance with a default {@link Label}.
   *
   * <p>The label is initialized with default JavaFX properties and can be customized using the
   * builder methods.
   */
  public LabelBuilder() {
    this.label = new Label();
  }

  /**
   * Sets the text displayed in the label.
   *
   * <p>This text will be shown as the label's content.
   *
   * @param text the text to display
   * @return this builder instance for method chaining
   * @see Label#setText(String)
   */
  public LabelBuilder text(String text) {
    label.setText(text);
    return this;
  }

  /**
   * Adds style classes to the label.
   *
   * <p>These classes should be defined in your CSS stylesheet.
   *
   * @param styleClasses one or more CSS style classes to apply
   * @return this builder instance for method chaining
   * @see javafx.scene.Node#getStyleClass()
   */
  public LabelBuilder styleClass(String... styleClasses) {
    label.getStyleClass().addAll(styleClasses);
    return this;
  }

  /**
   * Sets the text alignment within the label.
   *
   * <p>This affects how the text is aligned horizontally within the label's bounds.
   *
   * @param alignment the text alignment to use
   * @return this builder instance for method chaining
   * @see Label#setTextAlignment(TextAlignment)
   */
  public LabelBuilder textAlignment(TextAlignment alignment) {
    label.setTextAlignment(alignment);
    return this;
  }

  /**
   * Sets whether the text should wrap if it exceeds the label's width.
   *
   * <p>When enabled, text that is too long will wrap to the next line instead of being clipped.
   *
   * @param wrap true to enable text wrapping, false to disable it
   * @return this builder instance for method chaining
   * @see Label#setWrapText(boolean)
   */
  public LabelBuilder wrapText(boolean wrap) {
    label.setWrapText(wrap);
    return this;
  }

  /**
   * Builds and returns the configured {@link Label} instance.
   *
   * <p>After this call, the label is ready to be added to a scene graph.
   *
   * @return the configured JavaFX {@link Label}
   */
  public Label build() {
    return label;
  }
}

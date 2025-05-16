package edu.ntnu.stud.boardgame.core.view.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;

/**
 * A customized JavaFX CheckBox with enhanced styling options and a builder API. This class extends
 * the standard JavaFX CheckBox to provide additional functionality and easier configuration through
 * a builder pattern.
 *
 * <p>Example usage:</p>
 * <pre>
 * CheckBox rememberMe = CheckBox.builder()
 *     .text("Remember me")
 *     .selected(true)
 *     .onAction(e -> handleRememberMe())
 *     .build();
 * </pre>
 */
public class CheckBox extends javafx.scene.control.CheckBox {

  /**
   * Constructs a CheckBox with no text.
   */
  public CheckBox() {
    super();
    initialize();
  }

  /**
   * Constructs a CheckBox with the specified text.
   *
   * @param text the text to display next to the checkbox
   */
  public CheckBox(String text) {
    super(text);
    initialize();
  }

  /**
   * Initializes the checkbox with default settings. Sets the cursor to a hand pointer when hovering
   * over the checkbox.
   */
  private void initialize() {
    setCursor(Cursor.HAND);
  }

  /**
   * Creates a new checkbox builder instance to configure a checkbox.
   *
   * @return a new builder instance for creating a CheckBox
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder class for creating customized CheckBox instances. This pattern allows for more readable
   * and maintainable code when configuring checkboxes with multiple properties.
   */
  public static class Builder {

    private String text;
    private String id;
    private String styleClass;
    private String style;
    private boolean selected;
    private boolean indeterminate;
    private boolean allowIndeterminate;
    private boolean disabled;
    private EventHandler<ActionEvent> onAction;

    /**
     * Creates a new builder instance with default settings.
     */
    private Builder() {
      this.text = "";
      this.selected = false;
      this.indeterminate = false;
      this.allowIndeterminate = false;
      this.disabled = false;
    }

    /**
     * Sets the text to display next to the checkbox.
     *
     * @param text the text to display
     * @return this builder for method chaining
     */
    public Builder text(String text) {
      this.text = text;
      return this;
    }

    /**
     * Sets the CSS ID of the checkbox.
     *
     * @param id the CSS ID
     * @return this builder for method chaining
     */
    public Builder id(String id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a CSS style class to the checkbox.
     *
     * @param styleClass the CSS style class to add
     * @return this builder for method chaining
     */
    public Builder styleClass(String styleClass) {
      this.styleClass = styleClass;
      return this;
    }

    /**
     * Sets inline CSS style for the checkbox.
     *
     * @param style the CSS style string
     * @return this builder for method chaining
     */
    public Builder style(String style) {
      this.style = style;
      return this;
    }

    /**
     * Sets whether the checkbox is initially selected.
     *
     * @param selected true if the checkbox should be selected, false otherwise
     * @return this builder for method chaining
     */
    public Builder selected(boolean selected) {
      this.selected = selected;
      return this;
    }

    /**
     * Sets whether the checkbox is in the indeterminate state. This is a state where the checkbox
     * is neither checked nor unchecked.
     *
     * @param indeterminate true if the checkbox should be in the indeterminate state
     * @return this builder for method chaining
     */
    public Builder indeterminate(boolean indeterminate) {
      this.indeterminate = indeterminate;
      return this;
    }

    /**
     * Sets whether the checkbox allows the indeterminate state.
     *
     * @param allowIndeterminate true if the checkbox should allow the indeterminate state
     * @return this builder for method chaining
     */
    public Builder allowIndeterminate(boolean allowIndeterminate) {
      this.allowIndeterminate = allowIndeterminate;
      return this;
    }

    /**
     * Sets whether the checkbox is disabled.
     *
     * @param disabled true to disable the checkbox, false to enable it
     * @return this builder for method chaining
     */
    public Builder disabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    /**
     * Sets the action handler to be called when the checkbox's state changes.
     *
     * @param handler the event handler for checkbox state changes
     * @return this builder for method chaining
     */
    public Builder onAction(EventHandler<ActionEvent> handler) {
      this.onAction = handler;
      return this;
    }

    /**
     * Builds a new CheckBox instance with the configured properties.
     *
     * @return a new CheckBox instance
     */
    public CheckBox build() {
      CheckBox checkBox = new CheckBox(text);

      if (id != null) {
        checkBox.setId(id);
      }

      if (styleClass != null) {
        checkBox.getStyleClass().add(styleClass);
      }

      if (style != null) {
        checkBox.setStyle(style);
      }

      checkBox.setSelected(selected);
      checkBox.setIndeterminate(indeterminate);
      checkBox.setAllowIndeterminate(allowIndeterminate);
      checkBox.setDisable(disabled);

      if (onAction != null) {
        checkBox.setOnAction(onAction);
      }

      return checkBox;
    }
  }
}
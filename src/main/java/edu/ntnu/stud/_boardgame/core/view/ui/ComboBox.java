package edu.ntnu.stud.boardgame.core.view.ui;

import java.util.Collection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SingleSelectionModel;

/**
 * A customized JavaFX ComboBox with enhanced styling options and a builder API. This class extends
 * the standard JavaFX ComboBox to provide additional functionality and easier configuration through
 * a builder pattern.
 *
 * <p>The ComboBox provides a dropdown list of options from which a user can select one item.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * ComboBox&lt;String&gt; countrySelector = ComboBox.&lt;String&gt;builder()
 *     .items("USA", "Canada", "Mexico")
 *     .promptText("Select a country")
 *     .onAction(e -> handleCountrySelection())
 *     .build();
 * </pre>
 *
 * @param <T> the type of items to be stored in the ComboBox
 */
public class ComboBox<T> extends javafx.scene.control.ComboBox<T> {

  /**
   * Constructs a ComboBox with an empty list of items.
   */
  public ComboBox() {
    super();
    initialize();
  }

  /**
   * Constructs a ComboBox with the specified list of items.
   *
   * @param items the initial items to display in the ComboBox
   */
  public ComboBox(ObservableList<T> items) {
    super(items);
    initialize();
  }

  /**
   * Initializes the ComboBox with default settings. Adds the standard CSS style class.
   */
  private void initialize() {
    getStyleClass().add("combo-box");
  }

  /**
   * Creates a new ComboBox builder instance to configure a ComboBox.
   *
   * @param <T> the type of items to be stored in the ComboBox
   * @return a new builder instance for creating a ComboBox
   */
  public static <T> Builder<T> builder() {
    return new Builder<>();
  }

  /**
   * Builder class for creating customized ComboBox instances. This pattern allows for more readable
   * and maintainable code when configuring ComboBoxes with multiple properties.
   *
   * @param <T> the type of items to be stored in the ComboBox
   */
  public static class Builder<T> {

    private ObservableList<T> items;
    private T selectedItem;
    private int selectedIndex;
    private String id;
    private String styleClass;
    private String style;
    private double width;
    private double height;
    private String promptText;
    private boolean editable;
    private boolean disabled;
    private boolean rounded;
    private EventHandler<ActionEvent> onAction;

    /**
     * Creates a new builder instance with default settings.
     */
    private Builder() {
      this.items = FXCollections.observableArrayList();
      this.selectedIndex = -1;
      this.width = -1;
      this.height = -1;
      this.editable = false;
      this.disabled = false;
      this.rounded = false;
    }

    /**
     * Sets the items to display in the ComboBox from a collection.
     *
     * @param items the collection of items to display
     * @return this builder for method chaining
     */
    public Builder<T> items(Collection<T> items) {
      this.items = FXCollections.observableArrayList(items);
      return this;
    }

    /**
     * Sets the items to display in the ComboBox from a list.
     *
     * @param items the list of items to display
     * @return this builder for method chaining
     */
    public Builder<T> items(List<T> items) {
      this.items = FXCollections.observableArrayList(items);
      return this;
    }

    /**
     * Sets the items to display in the ComboBox from varargs.
     *
     * @param items the items to display
     * @return this builder for method chaining
     */
    @SafeVarargs
    public final Builder<T> items(T... items) {
      this.items = FXCollections.observableArrayList(items);
      return this;
    }

    /**
     * Sets the initially selected item in the ComboBox.
     *
     * @param selectedItem the item to select initially
     * @return this builder for method chaining
     */
    public Builder<T> selectedItem(T selectedItem) {
      this.selectedItem = selectedItem;
      return this;
    }

    /**
     * Sets the initially selected index in the ComboBox.
     *
     * @param selectedIndex the index to select initially
     * @return this builder for method chaining
     */
    public Builder<T> selectedIndex(int selectedIndex) {
      this.selectedIndex = selectedIndex;
      return this;
    }

    /**
     * Sets the CSS ID of the ComboBox.
     *
     * @param id the CSS ID
     * @return this builder for method chaining
     */
    public Builder<T> id(String id) {
      this.id = id;
      return this;
    }

    /**
     * Adds a CSS style class to the ComboBox.
     *
     * @param styleClass the CSS style class to add
     * @return this builder for method chaining
     */
    public Builder<T> styleClass(String styleClass) {
      this.styleClass = styleClass;
      return this;
    }

    /**
     * Sets inline CSS style for the ComboBox.
     *
     * @param style the CSS style string
     * @return this builder for method chaining
     */
    public Builder<T> style(String style) {
      this.style = style;
      return this;
    }

    /**
     * Sets the preferred width of the ComboBox.
     *
     * @param width the preferred width in pixels
     * @return this builder for method chaining
     */
    public Builder<T> width(double width) {
      this.width = width;
      return this;
    }

    /**
     * Sets the preferred height of the ComboBox.
     *
     * @param height the preferred height in pixels
     * @return this builder for method chaining
     */
    public Builder<T> height(double height) {
      this.height = height;
      return this;
    }

    /**
     * Sets the prompt text to display when no item is selected.
     *
     * @param promptText the prompt text to display
     * @return this builder for method chaining
     */
    public Builder<T> promptText(String promptText) {
      this.promptText = promptText;
      return this;
    }

    /**
     * Sets whether the ComboBox is editable, allowing users to enter values not in the list.
     *
     * @param editable true if the ComboBox should be editable, false otherwise
     * @return this builder for method chaining
     */
    public Builder<T> editable(boolean editable) {
      this.editable = editable;
      return this;
    }

    /**
     * Sets whether the ComboBox is disabled.
     *
     * @param disabled true to disable the ComboBox, false to enable it
     * @return this builder for method chaining
     */
    public Builder<T> disabled(boolean disabled) {
      this.disabled = disabled;
      return this;
    }

    /**
     * Sets whether the ComboBox should have rounded corners.
     *
     * @param rounded true for rounded corners, false for square corners
     * @return this builder for method chaining
     */
    public Builder<T> rounded(boolean rounded) {
      this.rounded = rounded;
      return this;
    }

    /**
     * Sets the action handler to be called when an item is selected.
     *
     * @param handler the event handler for item selection
     * @return this builder for method chaining
     */
    public Builder<T> onAction(EventHandler<ActionEvent> handler) {
      this.onAction = handler;
      return this;
    }

    /**
     * Builds a new ComboBox instance with the configured properties.
     *
     * @return a new ComboBox instance
     */
    public ComboBox<T> build() {
      ComboBox<T> comboBox = new ComboBox<>(items);

      if (id != null) {
        comboBox.setId(id);
      }

      if (styleClass != null) {
        comboBox.getStyleClass().add(styleClass);
      }

      if (rounded) {
        comboBox.getStyleClass().add("rounded");
      }

      if (style != null) {
        comboBox.setStyle(style);
      }

      if (width > 0) {
        comboBox.setPrefWidth(width);
      }

      if (height > 0) {
        comboBox.setPrefHeight(height);
      }

      if (promptText != null) {
        comboBox.setPromptText(promptText);
      }

      comboBox.setEditable(editable);
      comboBox.setDisable(disabled);

      SingleSelectionModel<T> selectionModel = comboBox.getSelectionModel();
      if (selectedItem != null) {
        selectionModel.select(selectedItem);
      } else if (selectedIndex >= 0 && selectedIndex < items.size()) {
        selectionModel.select(selectedIndex);
      }

      if (onAction != null) {
        comboBox.setOnAction(onAction);
      }

      return comboBox;
    }
  }
}
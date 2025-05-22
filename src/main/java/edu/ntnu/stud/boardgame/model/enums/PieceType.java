package edu.ntnu.stud.boardgame.model.enums;

import javafx.scene.paint.Color;

/**
 * Represents the available game piece types with their associated colors.
 */
public enum PieceType {
  RED("RedToken", Color.RED),
  BLUE("BlueToken", Color.BLUE),
  GREEN("GreenToken", Color.GREEN),
  YELLOW("YellowToken", Color.YELLOW),
  BLACK("BlackToken", Color.BLACK);

  private final String displayName;
  private final Color color;

  PieceType(String displayName, Color color) {
    this.displayName = displayName;
    this.color = color;
  }

  /**
   * Creates a PieceType from its display name.
   *
   * @param displayName the display name to match
   * @return the matching PieceType
   * @throws IllegalArgumentException if no match is found or displayName is
   *                                  invalid
   */
  public static PieceType fromDisplayName(String displayName) {
    if (displayName == null || displayName.trim().isEmpty()) {
      throw new IllegalArgumentException("Display name cannot be null or empty when parsing PieceType.");
    }
    for (PieceType type : values()) {
      if (type.getDisplayName().equalsIgnoreCase(displayName)) {
        return type;
      }
    }
    throw new IllegalArgumentException("No piece type with display name: " + displayName);
  }

  /**
   * Gets the display name of the piece type.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Gets the color associated with this piece type.
   *
   * @return the piece color
   */
  public Color getColor() {
    return color;
  }

  /**
   * Gets a formatted version of the display name with spaces before capital
   * letters.
   *
   * @return the formatted display name
   */
  public String getFormattedDisplayName() {
    return displayName.replaceAll("(?<!^)([A-Z])", " $1");
  }

  @Override
  public String toString() {
    return getFormattedDisplayName();
  }
}
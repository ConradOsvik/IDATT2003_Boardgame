package edu.ntnu.stud.boardgame.model.enums;

import javafx.scene.paint.Color;

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

  public String getDisplayName() {
    return displayName;
  }

  public Color getColor() {
    return color;
  }

  public String getFormattedDisplayName() {
    return displayName.replaceAll("(?<!^)([A-Z])", " $1");
  }

  @Override
  public String toString() {
    return getFormattedDisplayName();
  }
}
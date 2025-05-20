package edu.ntnu.stud.boardgame.model.enums;

public enum PieceType {
  RED("RedToken"), BLUE("BlueToken"), GREEN("GreenToken"), YELLOW("YellowToken"), BLACK(
      "BlackToken");

  private final String displayName;

  PieceType(String displayName) {
    this.displayName = displayName;
  }

  public static PieceType fromDisplayName(String displayName) {
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

  public String getFormattedDisplayName() {
    return displayName.replaceAll("(?<!^)([A-Z])", " $1");
  }

  @Override
  public String toString() {
    return getFormattedDisplayName();
  }
}
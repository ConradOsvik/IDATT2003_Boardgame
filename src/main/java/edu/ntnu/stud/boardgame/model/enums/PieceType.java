package edu.ntnu.stud.boardgame.model.enums;

public enum PieceType {
  RED("Red token"), BLUE("Blue token"), GREEN("Green token"), YELLOW("Yellow token"), BLACK(
      "Black token");

  private final String displayName;

  PieceType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}
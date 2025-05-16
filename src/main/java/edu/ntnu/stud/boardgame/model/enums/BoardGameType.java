package edu.ntnu.stud.boardgame.model.enums;

public enum BoardGameType {
  LADDER("Snakes and Ladders"), MONOPOLY("Monopoly");

  private final String displayName;

  BoardGameType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}

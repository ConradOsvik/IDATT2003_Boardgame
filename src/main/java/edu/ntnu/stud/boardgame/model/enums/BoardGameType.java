package edu.ntnu.stud.boardgame.model.enums;

/** Represents the available board game types in the application. */
public enum BoardGameType {
  LADDER("Snakes and Ladders"),
  MONOPOLY("Monopoly");

  private final String displayName;

  BoardGameType(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Gets the display name of the game type.
   *
   * @return the formatted display name
   */
  public String getDisplayName() {
    return displayName;
  }
}

package edu.ntnu.stud.boardgame.snakesandladders.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SlPlayerTest {

  private SlPlayer player;
  private final String PLAYER_NAME = "TestPlayer";
  private final Color PLAYER_COLOR = Color.RED;

  @BeforeEach
  void setUp() {
    player = new SlPlayer(PLAYER_NAME, PLAYER_COLOR);
  }

  @Test
  void constructor_validArgs_createsPlayer() {
    assertEquals(PLAYER_NAME, player.getName());
    assertEquals(PLAYER_COLOR, player.getColor());
  }

  @Test
  void constructor_nullName_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new SlPlayer(null, PLAYER_COLOR));
  }

  @Test
  void constructor_emptyName_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new SlPlayer("", PLAYER_COLOR));
  }

  @Test
  void constructor_nullColor_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new SlPlayer(PLAYER_NAME, null));
  }

  @Test
  void getColor_returnsCorrectColor() {
    assertEquals(PLAYER_COLOR, player.getColor());
  }
}
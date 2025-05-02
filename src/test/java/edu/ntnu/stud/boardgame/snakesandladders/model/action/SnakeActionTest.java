package edu.ntnu.stud.boardgame.snakesandladders.model.action;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeActionTest {

  private SnakeAction snakeAction;
  private Tile tailTile;

  @BeforeEach
  void setUp() {
    tailTile = new Tile(3);
    snakeAction = new SnakeAction(tailTile);
  }

  @Test
  void constructor_nullTile_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new SnakeAction(null));
  }

  @Test
  void perform_validPlayer_movesPlayerToTailTile() {
    Player player = mock(Player.class);

    snakeAction.perform(player);

    verify(player).placeOnTile(tailTile);
  }

  @Test
  void perform_nullPlayer_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> snakeAction.perform(null));
  }
}
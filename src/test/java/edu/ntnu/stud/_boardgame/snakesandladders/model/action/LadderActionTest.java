package edu.ntnu.stud.boardgame.snakesandladders.model.action;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.model.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LadderActionTest {

  private LadderAction ladderAction;
  private Tile topTile;

  @BeforeEach
  void setUp() {
    topTile = new Tile(10);
    ladderAction = new LadderAction(topTile);
  }

  @Test
  void constructor_nullTile_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new LadderAction(null));
  }

  @Test
  void perform_validPlayer_movesPlayerToTopTile() {
    Player player = mock(Player.class);

    ladderAction.perform(player);

    verify(player).placeOnTile(topTile);
  }

  @Test
  void perform_nullPlayer_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> ladderAction.perform(null));
  }
}
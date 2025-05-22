package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import edu.ntnu.stud.boardgame.model.action.TileAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("Tile Tests")
class TileTest {

  private Tile tile;

  @Mock private Tile nextTile;

  @Mock private TileAction mockAction;

  @Mock private Player mockPlayer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    tile = new Tile(5);
  }

  @Test
  @DisplayName("getTileId should return the correct tile ID")
  void getTileId_anyTile_returnsCorrectId() {
    assertEquals(5, tile.getTileId());
  }

  @Test
  @DisplayName("setNextTile should link to the correct next tile")
  void setNextTile_validTile_linksCorrectly() {
    tile.setNextTile(nextTile);

    assertEquals(nextTile, tile.getNextTile());
  }

  @Test
  @DisplayName("setLandAction should set the correct action")
  void setLandAction_validAction_setsCorrectly() {
    tile.setLandAction(mockAction);

    assertEquals(mockAction, tile.getLandAction());
  }

  @Test
  @DisplayName("setName should set the correct name")
  void setName_validName_setsCorrectly() {
    tile.setName("Test Tile");

    assertEquals("Test Tile", tile.getName());
  }

  @Nested
  @DisplayName("landPlayer Tests")
  class LandPlayerTests {

    @Test
    @DisplayName("landPlayer with action should perform the action")
    void landPlayer_withAction_performsAction() {
      tile.setLandAction(mockAction);

      tile.landPlayer(mockPlayer);

      verify(mockAction).perform(mockPlayer);
    }

    @Test
    @DisplayName("landPlayer without action should do nothing")
    void landPlayer_withoutAction_doesNothing() {
      tile.landPlayer(mockPlayer);

      verifyNoInteractions(mockPlayer);
    }
  }

  @Nested
  @DisplayName("Coordinate Tests")
  class CoordinateTests {

    @Test
    @DisplayName("setRow and setColumn should set correct coordinates")
    void setRowAndColumn_validValues_setsCorrectly() {
      tile.setRow(3);
      tile.setColumn(4);

      assertEquals(3, tile.getRow());
      assertEquals(4, tile.getColumn());
    }
  }
}

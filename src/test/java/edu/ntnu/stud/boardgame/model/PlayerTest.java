package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.model.enums.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("Player Tests")
class PlayerTest {

  private Player player;

  @Mock private Tile mockTile;

  @Mock private Tile mockNextTile;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    player = new Player("TestPlayer", PieceType.RED);
  }

  @Nested
  @DisplayName("Player Properties Tests")
  class PlayerPropertiesTests {

    @Test
    @DisplayName("getName should return correct name")
    void getName_anyPlayer_returnsCorrectName() {
      assertEquals("TestPlayer", player.getName());
    }

    @Test
    @DisplayName("getPiece should return correct piece type")
    void getPiece_anyPlayer_returnsCorrectPieceType() {
      assertEquals(PieceType.RED, player.getPiece());
    }
  }

  @Nested
  @DisplayName("Tile Interaction Tests")
  class TileInteractionTests {

    @Test
    @DisplayName("placeOnTile should place player on tile and trigger landPlayer")
    void placeOnTile_validTile_placesPlayerAndTriggersLand() {
      player.placeOnTile(mockTile);

      assertEquals(mockTile, player.getCurrentTile());
      verify(mockTile).landPlayer(player);
    }

    @Test
    @DisplayName("getDestinationTile with zero steps should return current tile")
    void getDestinationTile_zeroSteps_returnsCurrentTile() {
      player.setCurrentTile(mockTile);

      Tile result = player.getDestinationTile(0);

      assertEquals(mockTile, result);
    }

    @Test
    @DisplayName("getDestinationTile with positive steps should navigate to correct tile")
    void getDestinationTile_positiveSteps_returnsCorrectTile() {
      player.setCurrentTile(mockTile);
      when(mockTile.getNextTile()).thenReturn(mockNextTile);
      when(mockNextTile.getNextTile()).thenReturn(null);

      Tile result = player.getDestinationTile(2);

      assertEquals(mockNextTile, result);
    }

    @Test
    @DisplayName("getDestinationTile with negative steps should throw exception")
    void getDestinationTile_negativeSteps_throwsException() {
      player.setCurrentTile(mockTile);

      assertThrows(IllegalArgumentException.class, () -> player.getDestinationTile(-1));
    }

    @Test
    @DisplayName("move should place player on destination tile")
    void move_validSteps_placesPlayerOnDestinationTile() {
      player.setCurrentTile(mockTile);
      when(mockTile.getNextTile()).thenReturn(mockNextTile);

      player.move(1);

      assertEquals(mockNextTile, player.getCurrentTile());
      verify(mockNextTile).landPlayer(player);
    }
  }

  @Nested
  @DisplayName("Turn Skipping Tests")
  class TurnSkippingTests {

    @Test
    @DisplayName("shouldSkipNextTurn initially should return false")
    void shouldSkipNextTurn_newPlayer_returnsFalse() {
      assertFalse(player.shouldSkipNextTurn());
    }

    @Test
    @DisplayName("setSkipNextTurn to true should set flag correctly")
    void setSkipNextTurn_setToTrue_flagSetCorrectly() {
      player.setSkipNextTurn(true);

      assertTrue(player.shouldSkipNextTurn());
    }

    @Test
    @DisplayName("setSkipNextTurn to false should reset flag")
    void setSkipNextTurn_setToFalse_flagResetCorrectly() {
      player.setSkipNextTurn(true);

      player.setSkipNextTurn(false);

      assertFalse(player.shouldSkipNextTurn());
    }
  }
}

package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@DisplayName("Action Tests")
class ActionTests {

  @Nested
  @DisplayName("LadderAction Tests")
  class LadderActionTests {

    private LadderAction ladderAction;

    @Mock
    private Tile destinationTile;

    @Mock
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
      MockitoAnnotations.openMocks(this);
      ladderAction = new LadderAction(destinationTile);
    }

    @Test
    @DisplayName("perform should place player on destination tile")
    void perform_validPlayer_placesPlayerOnDestinationTile() {
      ladderAction.perform(mockPlayer);

      verify(mockPlayer).placeOnTile(destinationTile);
    }

    @Test
    @DisplayName("getDestinationTile should return correct destination")
    void getDestinationTile_anyAction_returnsCorrectDestination() {
      Tile result = ladderAction.getDestinationTile();

      assertEquals(destinationTile, result);
    }

    @Test
    @DisplayName("Constructor with null destination should throw IllegalArgumentException")
    void constructor_nullDestination_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> new LadderAction(null));
    }

    @Test
    @DisplayName("perform with null player should throw IllegalArgumentException")
    void perform_nullPlayer_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> ladderAction.perform(null));
    }
  }

  @Nested
  @DisplayName("SnakeAction Tests")
  class SnakeActionTests {

    private SnakeAction snakeAction;

    @Mock
    private Tile destinationTile;

    @Mock
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
      MockitoAnnotations.openMocks(this);
      snakeAction = new SnakeAction(destinationTile);
    }

    @Test
    @DisplayName("perform should place player on destination tile")
    void perform_validPlayer_placesPlayerOnDestinationTile() {
      snakeAction.perform(mockPlayer);

      verify(mockPlayer).placeOnTile(destinationTile);
    }

    @Test
    @DisplayName("getDestinationTile should return correct destination")
    void getDestinationTile_anyAction_returnsCorrectDestination() {
      Tile result = snakeAction.getDestinationTile();

      assertEquals(destinationTile, result);
    }

    @Test
    @DisplayName("Constructor with null destination should throw IllegalArgumentException")
    void constructor_nullDestination_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> new SnakeAction(null));
    }

    @Test
    @DisplayName("perform with null player should throw IllegalArgumentException")
    void perform_nullPlayer_throwsIllegalArgumentException() {
      assertThrows(IllegalArgumentException.class, () -> snakeAction.perform(null));
    }
  }

  @Nested
  @DisplayName("SkipTurnAction Tests")
  class SkipTurnActionTests {

    private SkipTurnAction skipTurnAction;

    @Mock
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
      MockitoAnnotations.openMocks(this);
      skipTurnAction = new SkipTurnAction();
    }

    @Test
    @DisplayName("perform should set player to skip next turn")
    void perform_validPlayer_setsSkipNextTurn() {
      skipTurnAction.perform(mockPlayer);

      verify(mockPlayer).setSkipNextTurn(true);
    }
  }
}
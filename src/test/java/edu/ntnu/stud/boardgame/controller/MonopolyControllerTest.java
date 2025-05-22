package edu.ntnu.stud.boardgame.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.controller.MonopolyController.DiceRollResult;
import edu.ntnu.stud.boardgame.controller.MonopolyController.GameRestartResult;
import edu.ntnu.stud.boardgame.controller.MonopolyController.PlayerActionState;
import edu.ntnu.stud.boardgame.controller.MonopolyController.PropertyPurchaseResult;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.TileAction;
import edu.ntnu.stud.boardgame.model.game.BoardGame;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MonopolyControllerTest {

  @Mock
  private GameController gameController;

  @Mock
  private MonopolyGame mockMonopolyGame;

  @Mock
  private BoardGame mockOtherGame;

  @Mock
  private Player mockPlayer;

  @Mock
  private Tile mockTile;

  @Mock
  private PropertyAction mockPropertyAction;

  @Mock
  private TileAction mockNonPropertyAction;

  private MonopolyController monopolyController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    monopolyController = new MonopolyController(gameController);
  }

  // Constructor and Game Reference Tests
  @Test
  void constructor_initializesCorrectly() {
    assertNotNull(monopolyController);
  }

  @Test
  void updateGameReference_whenGameIsMonopoly_setsMonopolyGame() {
    when(gameController.getGame()).thenReturn(mockMonopolyGame);

    monopolyController.updateGameReference();

    assertEquals(mockMonopolyGame, monopolyController.getGame());
  }

  @Test
  void updateGameReference_whenGameIsNotMonopoly_setsGameToNull() {
    when(gameController.getGame()).thenReturn(mockOtherGame);

    monopolyController.updateGameReference();

    assertNull(monopolyController.getGame());
  }

  @Test
  void updateGameReference_whenGameIsNull_setsGameToNull() {
    when(gameController.getGame()).thenReturn(null);

    monopolyController.updateGameReference();

    assertNull(monopolyController.getGame());
  }

  // Property Purchase Tests
  @Test
  void attemptPropertyPurchase_successfulPurchase_returnsSuccessResult() {
    // Arrange
    setupSuccessfulPurchaseScenario();
    when(mockMonopolyGame.buyProperty(mockPlayer, mockTile)).thenReturn(true);
    when(mockTile.getName()).thenReturn("Test Property");
    when(mockPropertyAction.getPrice()).thenReturn(100);

    // Act
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    // Assert
    assertTrue(result.isSuccess());
    assertTrue(result.getMessage().contains("successfully purchased"));
    assertTrue(result.getMessage().contains("Test Property"));
    assertTrue(result.getMessage().contains("$100"));
    verify(mockMonopolyGame).buyProperty(mockPlayer, mockTile);
  }

  @Test
  void attemptPropertyPurchase_insufficientFunds_returnsFailureResult() {
    // Arrange
    setupSuccessfulPurchaseScenario();
    when(mockMonopolyGame.buyProperty(mockPlayer, mockTile)).thenReturn(false);

    // Act
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("Purchase failed - insufficient funds", result.getMessage());
  }

  @Test
  void attemptPropertyPurchase_noActiveGame_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(null);
    monopolyController.updateGameReference();

    // Act
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("No active Monopoly game found", result.getMessage());
  }

  @Test
  void attemptPropertyPurchase_noCurrentPlayer_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(null);

    // Act
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("No current player found", result.getMessage());
  }

  @Test
  void attemptPropertyPurchase_playerNotOnTile_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockPlayer.getCurrentTile()).thenReturn(null);

    // Act
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("Player is not on any tile", result.getMessage());
  }

  @Test
  void attemptPropertyPurchase_playerIsBankrupt_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockPlayer.getCurrentTile()).thenReturn(mockTile);
    when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(true);

    // Act
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("Player is bankrupt and cannot purchase properties", result.getMessage());
  }

  @Test
  void attemptPropertyPurchase_cannotBuyProperty_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockPlayer.getCurrentTile()).thenReturn(mockTile);
    when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(false);
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getOwner()).thenReturn(mockPlayer); // Already owned

    // Act
    PropertyPurchaseResult result = monopolyController.attemptPropertyPurchase();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("This property cannot be purchased", result.getMessage());
  }

  // Dice Roll Tests
  @Test
  void attemptDiceRoll_successfulRoll_returnsSuccessResult() {
    // Arrange
    setupValidGameState();
    when(gameController.playTurn()).thenReturn(true);

    // Act
    DiceRollResult result = monopolyController.attemptDiceRoll();

    // Assert
    assertTrue(result.isSuccess());
    assertEquals("Turn completed successfully", result.getMessage());
    verify(gameController).playTurn();
  }

  @Test
  void attemptDiceRoll_failedRoll_returnsFailureResult() {
    // Arrange
    setupValidGameState();
    when(gameController.playTurn()).thenReturn(false);

    // Act
    DiceRollResult result = monopolyController.attemptDiceRoll();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("Turn failed", result.getMessage());
  }

  @Test
  void attemptDiceRoll_noActiveGame_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(null);
    monopolyController.updateGameReference();

    // Act
    DiceRollResult result = monopolyController.attemptDiceRoll();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("No active Monopoly game found", result.getMessage());
  }

  @Test
  void attemptDiceRoll_noCurrentPlayer_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(null);

    // Act
    DiceRollResult result = monopolyController.attemptDiceRoll();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("No current player found", result.getMessage());
  }

  @Test
  void attemptDiceRoll_playerIsBankrupt_returnsFailureResult() {
    // Arrange
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(true);

    // Act
    DiceRollResult result = monopolyController.attemptDiceRoll();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("Current player is bankrupt and cannot roll", result.getMessage());
  }

  // Game Restart Tests
  @Test
  void attemptGameRestart_successfulRestart_returnsSuccessResult() {
    // Arrange
    when(gameController.startGame()).thenReturn(true);

    // Act
    GameRestartResult result = monopolyController.attemptGameRestart();

    // Assert
    assertTrue(result.isSuccess());
    assertEquals("Game restarted successfully", result.getMessage());
    verify(gameController).startGame();
  }

  @Test
  void attemptGameRestart_failedRestart_returnsFailureResult() {
    // Arrange
    when(gameController.startGame()).thenReturn(false);

    // Act
    GameRestartResult result = monopolyController.attemptGameRestart();

    // Assert
    assertFalse(result.isSuccess());
    assertEquals("Failed to restart game", result.getMessage());
  }

  // Player Action State Tests
  @Test
  void getCurrentPlayerActionState_validStateCanBuy_returnsCorrectState() {
    // Arrange
    setupSuccessfulPurchaseScenario();

    // Act
    PlayerActionState state = monopolyController.getCurrentPlayerActionState();

    // Assert
    assertTrue(state.canRoll());
    assertTrue(state.canBuyProperty());
    assertEquals("Ready to play", state.getStatusMessage());
  }

  @Test
  void getCurrentPlayerActionState_validStateCannotBuy_returnsCorrectState() {
    // Arrange
    setupValidGameState();
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getOwner()).thenReturn(mockPlayer); // Already owned

    // Act
    PlayerActionState state = monopolyController.getCurrentPlayerActionState();

    // Assert
    assertTrue(state.canRoll());
    assertFalse(state.canBuyProperty());
    assertEquals("Ready to play", state.getStatusMessage());
  }

  @Test
  void getCurrentPlayerActionState_noActiveGame_returnsInvalidState() {
    // Arrange
    when(gameController.getGame()).thenReturn(null);
    monopolyController.updateGameReference();

    // Act
    PlayerActionState state = monopolyController.getCurrentPlayerActionState();

    // Assert
    assertFalse(state.canRoll());
    assertFalse(state.canBuyProperty());
    assertEquals("No active game", state.getStatusMessage());
  }

  @Test
  void getCurrentPlayerActionState_playerIsBankrupt_returnsInvalidState() {
    // Arrange
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(true);

    // Act
    PlayerActionState state = monopolyController.getCurrentPlayerActionState();

    // Assert
    assertFalse(state.canRoll());
    assertFalse(state.canBuyProperty());
    assertEquals("Player is bankrupt", state.getStatusMessage());
  }

  // Existing getter method tests (unchanged logic)
  @Test
  void canBuyProperty_canBuy_returnsTrue() {
    setupValidGameState();
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getOwner()).thenReturn(null);
    when(mockPropertyAction.getPrice()).thenReturn(100);
    when(mockMonopolyGame.getPlayerMoney(mockPlayer)).thenReturn(200);

    assertTrue(monopolyController.canBuyProperty(mockTile));
  }

  @Test
  void canBuyProperty_alreadyOwned_returnsFalse() {
    setupValidGameState();
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getOwner()).thenReturn(mock(Player.class));

    assertFalse(monopolyController.canBuyProperty(mockTile));
  }

  @Test
  void canBuyProperty_notEnoughMoney_returnsFalse() {
    setupValidGameState();
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getOwner()).thenReturn(null);
    when(mockPropertyAction.getPrice()).thenReturn(200);
    when(mockMonopolyGame.getPlayerMoney(mockPlayer)).thenReturn(100);

    assertFalse(monopolyController.canBuyProperty(mockTile));
  }

  @Test
  void canBuyProperty_nullGame_returnsFalse() {
    when(gameController.getGame()).thenReturn(null);
    monopolyController.updateGameReference();
    assertFalse(monopolyController.canBuyProperty(mockTile));
  }

  @Test
  void canBuyProperty_nullTile_returnsFalse() {
    assertFalse(monopolyController.canBuyProperty(null));
  }

  @Test
  void canBuyProperty_notPropertyAction_returnsFalse() {
    setupValidGameState();
    when(mockTile.getLandAction()).thenReturn(mockNonPropertyAction);
    assertFalse(monopolyController.canBuyProperty(mockTile));
  }

  @Test
  void getPropertyPrice_validProperty_returnsPrice() {
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getPrice()).thenReturn(150);
    assertEquals(150, monopolyController.getPropertyPrice(mockTile));
  }

  @Test
  void getPropertyPrice_nullTile_returnsZero() {
    assertEquals(0, monopolyController.getPropertyPrice(null));
  }

  @Test
  void getPropertyPrice_notPropertyAction_returnsZero() {
    when(mockTile.getLandAction()).thenReturn(mockNonPropertyAction);
    assertEquals(0, monopolyController.getPropertyPrice(mockTile));
  }

  @Test
  void getPropertyOwner_validProperty_returnsOwner() {
    Player owner = mock(Player.class);
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getOwner()).thenReturn(owner);
    assertEquals(owner, monopolyController.getPropertyOwner(mockTile));
  }

  @Test
  void getPropertyOwner_nullTile_returnsNull() {
    assertNull(monopolyController.getPropertyOwner(null));
  }

  @Test
  void getPropertyOwner_notPropertyAction_returnsNull() {
    when(mockTile.getLandAction()).thenReturn(mockNonPropertyAction);
    assertNull(monopolyController.getPropertyOwner(mockTile));
  }

  @Test
  void getPlayerMoney_validPlayer_returnsMoney() {
    setupValidGameState();
    when(mockMonopolyGame.getPlayerMoney(mockPlayer)).thenReturn(500);
    assertEquals(500, monopolyController.getPlayerMoney(mockPlayer));
  }

  @Test
  void getPlayerMoney_nullGame_returnsZero() {
    when(gameController.getGame()).thenReturn(null);
    monopolyController.updateGameReference();
    assertEquals(0, monopolyController.getPlayerMoney(mockPlayer));
  }

  @Test
  void getPlayerMoney_nullPlayer_returnsZero() {
    setupValidGameState();
    assertEquals(0, monopolyController.getPlayerMoney(null));
  }

  @Test
  void isPlayerBankrupt_isBankrupt_returnsTrue() {
    setupValidGameState();
    when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(true);
    assertTrue(monopolyController.isPlayerBankrupt(mockPlayer));
  }

  @Test
  void isPlayerBankrupt_isNotBankrupt_returnsFalse() {
    setupValidGameState();
    when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(false);
    assertFalse(monopolyController.isPlayerBankrupt(mockPlayer));
  }

  @Test
  void isPlayerBankrupt_nullGame_returnsFalse() {
    when(gameController.getGame()).thenReturn(null);
    monopolyController.updateGameReference();
    assertFalse(monopolyController.isPlayerBankrupt(mockPlayer));
  }

  @Test
  void isPlayerBankrupt_nullPlayer_returnsFalse() {
    setupValidGameState();
    assertFalse(monopolyController.isPlayerBankrupt(null));
  }

  // Helper methods to reduce test setup duplication
  private void setupValidGameState() {
    when(gameController.getGame()).thenReturn(mockMonopolyGame);
    monopolyController.updateGameReference();
    when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
    when(mockPlayer.getCurrentTile()).thenReturn(mockTile);
    when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(false);
  }

  private void setupSuccessfulPurchaseScenario() {
    setupValidGameState();
    when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
    when(mockPropertyAction.getOwner()).thenReturn(null);
    when(mockPropertyAction.getPrice()).thenReturn(100);
    when(mockMonopolyGame.getPlayerMoney(mockPlayer)).thenReturn(200);
  }
}
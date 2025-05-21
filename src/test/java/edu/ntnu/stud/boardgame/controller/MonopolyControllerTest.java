package edu.ntnu.stud.boardgame.controller;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonopolyControllerTest {

    @Mock
    private GameController gameController;

    @Mock
    private MonopolyGame mockMonopolyGame;

    @Mock
    private Player mockPlayer;

    @Mock
    private Tile mockTile;

    @Mock
    private PropertyAction mockPropertyAction;

    private MonopolyController monopolyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Instantiate MonopolyController here, after mocks are initialized.
        monopolyController = new MonopolyController(gameController);
        // Ensure that updateGameReference correctly sets up mockMonopolyGame
        when(gameController.getGame()).thenReturn(mockMonopolyGame);
        monopolyController.updateGameReference(); // Call this explicitly to set the game
    }

    @Test
    void constructor_and_updateGameReference_setsMonopolyGame() {
        assertNotNull(monopolyController.getGame());
        assertEquals(mockMonopolyGame, monopolyController.getGame());
    }

    @Test
    void updateGameReference_whenGameIsNotMonopoly_setsGameToNull() {
        when(gameController.getGame()).thenReturn(null); // Or a different game type
        monopolyController.updateGameReference();
        assertNull(monopolyController.getGame());
    }

    @Test
    void buyProperty_validPropertyAndPlayer_callsMonopolyGameBuyProperty() {
        when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockMonopolyGame.buyProperty(mockPlayer, mockTile)).thenReturn(true);

        boolean result = monopolyController.buyProperty(mockTile);

        assertTrue(result);
        verify(mockMonopolyGame).buyProperty(mockPlayer, mockTile);
    }

    @Test
    void buyProperty_nullGame_returnsFalse() {
        when(gameController.getGame()).thenReturn(null);
        monopolyController.updateGameReference(); // Game becomes null
        assertFalse(monopolyController.buyProperty(mockTile));
    }

    @Test
    void buyProperty_nullTile_returnsFalse() {
        assertFalse(monopolyController.buyProperty(null));
    }

    @Test
    void buyProperty_nullCurrentPlayer_returnsFalse() {
        when(mockMonopolyGame.getCurrentPlayer()).thenReturn(null);
        assertFalse(monopolyController.buyProperty(mockTile));
    }

    @Test
    void canBuyProperty_canBuy_returnsTrue() {
        when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
        when(mockPropertyAction.getOwner()).thenReturn(null);
        when(mockPropertyAction.getPrice()).thenReturn(100);
        when(mockMonopolyGame.getPlayerMoney(mockPlayer)).thenReturn(200);

        assertTrue(monopolyController.canBuyProperty(mockTile));
    }

    @Test
    void canBuyProperty_alreadyOwned_returnsFalse() {
        when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockTile.getLandAction()).thenReturn(mockPropertyAction);
        when(mockPropertyAction.getOwner()).thenReturn(mock(Player.class)); // Owned by someone

        assertFalse(monopolyController.canBuyProperty(mockTile));
    }

    @Test
    void canBuyProperty_notEnoughMoney_returnsFalse() {
        when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
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
    void canBuyProperty_nullCurrentPlayer_returnsFalse() {
        when(mockMonopolyGame.getCurrentPlayer()).thenReturn(null);
        assertFalse(monopolyController.canBuyProperty(mockTile));
    }

    @Test
    void canBuyProperty_notPropertyAction_returnsFalse() {
        when(mockMonopolyGame.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockTile.getLandAction()).thenReturn(null); // Not a PropertyAction
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
        when(mockTile.getLandAction()).thenReturn(null);
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
        when(mockTile.getLandAction()).thenReturn(null);
        assertNull(monopolyController.getPropertyOwner(mockTile));
    }

    @Test
    void getPlayerMoney_validPlayer_returnsMoney() {
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
        assertEquals(0, monopolyController.getPlayerMoney(null));
    }

    @Test
    void isPlayerBankrupt_isBankrupt_returnsTrue() {
        when(mockMonopolyGame.isBankrupt(mockPlayer)).thenReturn(true);
        assertTrue(monopolyController.isPlayerBankrupt(mockPlayer));
    }

    @Test
    void isPlayerBankrupt_isNotBankrupt_returnsFalse() {
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
        assertFalse(monopolyController.isPlayerBankrupt(null));
    }

    @Test
    void rollDice_callsGameControllerPlayTurn() {
        monopolyController.rollDice();
        verify(gameController).playTurn();
    }
}
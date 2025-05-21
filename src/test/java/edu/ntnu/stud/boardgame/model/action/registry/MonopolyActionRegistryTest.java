package edu.ntnu.stud.boardgame.model.action.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MonopolyActionRegistryTest {

    private MonopolyActionRegistry registry;
    private MonopolyGame mockGame;
    private Player mockPlayer;
    private PropertyAction mockPropertyAction;

    @BeforeEach
    void setUp() {
        registry = MonopolyActionRegistry.getInstance();
        registry.clearGame();

        mockGame = mock(MonopolyGame.class);
        mockPlayer = mock(Player.class);
        mockPropertyAction = mock(PropertyAction.class);
    }

    @AfterEach
    void tearDown() {
        registry.clearGame();
    }

    @Test
    void getInstance_shouldReturnSameInstance() {
        MonopolyActionRegistry instance1 = MonopolyActionRegistry.getInstance();
        MonopolyActionRegistry instance2 = MonopolyActionRegistry.getInstance();
        assertEquals(instance1, instance2, "getInstance should return the same singleton instance.");
    }

    @Test
    void registerGame_withNullGame_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            registry.registerGame(null);
        });
        assertEquals("Cannot register a null game in MonopolyActionRegistry.", exception.getMessage());
    }

    @Test
    void registerGame_withValidGame_shouldSetCurrentGame() {
        registry.registerGame(mockGame);
        registry.executeTaxAction(mockPlayer, 100);
        verify(mockGame, times(1)).payTax(mockPlayer, 100);
    }

    @Test
    void clearGame_shouldSetCurrentGameToNull() {
        registry.registerGame(mockGame);
        registry.clearGame();
        registry.executeTaxAction(mockPlayer, 100);
        verify(mockGame, never()).payTax(mockPlayer, 100);
    }

    @Test
    void executePropertyAction_whenNoGameRegistered_shouldLogWarningAndReturn() {
        registry.executePropertyAction(mockPlayer, mockPropertyAction);
        verify(mockGame, never()).payRent(any(Player.class), any(Player.class), anyInt());
    }

    @Test
    void executePropertyAction_withNullPlayer_shouldLogWarningAndReturn() {
        registry.registerGame(mockGame);
        registry.executePropertyAction(null, mockPropertyAction);
        verify(mockGame, never()).payRent(null, null, 0);
    }

    @Test
    void executePropertyAction_withNullAction_shouldLogWarningAndReturn() {
        registry.registerGame(mockGame);
        registry.executePropertyAction(mockPlayer, null);
        verify(mockGame, never()).payRent(null, null, 0);
    }

    @Test
    void executePropertyAction_propertyOwnedByAnotherPlayer_shouldCallPayRent() {
        registry.registerGame(mockGame);
        Player mockOwner = mock(Player.class);
        int price = 100;
        int expectedRent = price / 5;

        when(mockPropertyAction.getOwner()).thenReturn(mockOwner);
        when(mockPropertyAction.getPrice()).thenReturn(price);

        registry.executePropertyAction(mockPlayer, mockPropertyAction);
        verify(mockGame, times(1)).payRent(mockPlayer, mockOwner, expectedRent);
    }

    @Test
    void executePropertyAction_propertyOwnedBySamePlayer_shouldNotCallPayRent() {
        registry.registerGame(mockGame);
        when(mockPropertyAction.getOwner()).thenReturn(mockPlayer);

        registry.executePropertyAction(mockPlayer, mockPropertyAction);
        verify(mockGame, never()).payRent(mockPlayer, mockPlayer, 0);
    }

    @Test
    void executePropertyAction_propertyNotOwned_shouldNotCallPayRent() {
        registry.registerGame(mockGame);
        when(mockPropertyAction.getOwner()).thenReturn(null);

        registry.executePropertyAction(mockPlayer, mockPropertyAction);
        verify(mockGame, never()).payRent(mockPlayer, null, 0);
    }

    @Test
    void executeTaxAction_whenNoGameRegistered_shouldLogWarningAndReturn() {
        registry.executeTaxAction(mockPlayer, 50);
        verify(mockGame, never()).payTax(mockPlayer, 50);
    }

    @Test
    void executeTaxAction_withNullPlayer_shouldLogWarningAndReturn() {
        registry.registerGame(mockGame);
        registry.executeTaxAction(null, 50);
        verify(mockGame, never()).payTax(null, 50);
    }

    @Test
    void executeTaxAction_withNegativeAmount_shouldLogWarningAndReturn() {
        registry.registerGame(mockGame);
        registry.executeTaxAction(mockPlayer, -50);
        verify(mockGame, never()).payTax(mockPlayer, -50);
    }

    @Test
    void executeTaxAction_withValidArguments_shouldCallPayTax() {
        registry.registerGame(mockGame);
        int amount = 75;
        registry.executeTaxAction(mockPlayer, amount);
        verify(mockGame, times(1)).payTax(mockPlayer, amount);
    }

    @Test
    void executeStartAction_whenNoGameRegistered_shouldLogWarningAndReturn() {
        registry.executeStartAction(mockPlayer, 200);
        verify(mockGame, never()).receiveStartMoney(mockPlayer, 200);
    }

    @Test
    void executeStartAction_withNullPlayer_shouldLogWarningAndReturn() {
        registry.registerGame(mockGame);
        registry.executeStartAction(null, 200);
        verify(mockGame, never()).receiveStartMoney(null, 200);
    }

    @Test
    void executeStartAction_withNegativeAmount_shouldLogWarningAndReturn() {
        registry.registerGame(mockGame);
        registry.executeStartAction(mockPlayer, -200);
        verify(mockGame, never()).receiveStartMoney(mockPlayer, -200);
    }

    @Test
    void executeStartAction_withValidArguments_shouldCallReceiveStartMoney() {
        registry.registerGame(mockGame);
        int amount = 200;
        registry.executeStartAction(mockPlayer, amount);
        verify(mockGame, times(1)).receiveStartMoney(mockPlayer, amount);
    }
}
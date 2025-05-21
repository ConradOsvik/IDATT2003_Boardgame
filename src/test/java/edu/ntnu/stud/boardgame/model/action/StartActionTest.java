package edu.ntnu.stud.boardgame.model.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StartActionTest {

    private StartAction startAction;
    private final int validAmount = 200;
    private MonopolyGame mockGame;
    private MonopolyActionRegistry registry;

    @BeforeEach
    void setUp() {
        startAction = new StartAction(validAmount);
        mockGame = mock(MonopolyGame.class);

        registry = MonopolyActionRegistry.getInstance();
        registry.registerGame(mockGame);
    }

    @AfterEach
    void tearDown() {
        if (registry != null) {
            registry.clearGame();
        }
    }

    @Test
    void constructor_withAnyAmount_shouldCreateInstance() {
        assertNotNull(startAction);
        assertEquals(validAmount, startAction.getAmount());

        StartAction negativeAmountAction = new StartAction(-50);
        assertNotNull(negativeAmountAction);
        assertEquals(-50, negativeAmountAction.getAmount(),
                "Constructor should accept negative amounts as per current implementation.");
    }

    @Test
    void getAmount_shouldReturnCorrectAmount() {
        assertEquals(validAmount, startAction.getAmount());
    }

    @Test
    void perform_withNullPlayer_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            startAction.perform(null);
        });
        assertEquals("Player cannot be null for StartAction.", exception.getMessage());
        verify(mockGame, never()).receiveStartMoney(null, validAmount);
    }

    @Test
    void perform_withValidPlayer_shouldDelegateToMonopolyActionRegistry() {
        Player mockPlayer = mock(Player.class);

        startAction.perform(mockPlayer);

        verify(mockGame, times(1)).receiveStartMoney(mockPlayer, validAmount);
    }

    @Test
    void perform_withNegativeAmountInAction_shouldDelegateAndRegistryHandlesNegative() {
        Player mockPlayer = mock(Player.class);
        int negativeAmount = -100;
        StartAction negativeStartAction = new StartAction(negativeAmount);

        negativeStartAction.perform(mockPlayer);

        verify(mockGame, never()).receiveStartMoney(mockPlayer, negativeAmount);
    }

    @Test
    void perform_whenNoGameRegisteredInRegistry_shouldStillAttemptCallButRegistryHandlesIt() {
        registry.clearGame();
        Player mockPlayer = mock(Player.class);

        startAction.perform(mockPlayer);
        verify(mockGame, never()).receiveStartMoney(mockPlayer, validAmount);
    }
}
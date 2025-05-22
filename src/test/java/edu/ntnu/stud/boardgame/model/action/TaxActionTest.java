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

class TaxActionTest {

    private TaxAction taxAction;
    private final int validAmount = 50;
    private MonopolyGame mockGame;
    private MonopolyActionRegistry registry;

    @BeforeEach
    void setUp() {
        taxAction = new TaxAction(validAmount);
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
    void constructor_withValidAmount_shouldCreateInstance() {
        assertNotNull(taxAction);
        assertEquals(validAmount, taxAction.getAmount());
    }

    @Test
    void constructor_withNegativeAmount_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new TaxAction(-10);
        });
        assertEquals("Tax amount cannot be negative.", exception.getMessage());
    }

    @Test
    void getAmount_shouldReturnCorrectAmount() {
        assertEquals(validAmount, taxAction.getAmount());
    }

    @Test
    void perform_withNullPlayer_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taxAction.perform(null);
        });
        assertEquals("Player cannot be null for TaxAction.", exception.getMessage());
        verify(mockGame, never()).payTax(null, validAmount);
    }

    @Test
    void perform_withValidPlayer_shouldDelegateToMonopolyActionRegistry() {
        Player mockPlayer = mock(Player.class);

        taxAction.perform(mockPlayer);

        verify(mockGame, times(1)).payTax(mockPlayer, validAmount);
    }

    @Test
    void perform_whenNoGameRegisteredInRegistry_shouldStillAttemptCallButRegistryHandlesIt() {
        registry.clearGame();
        Player mockPlayer = mock(Player.class);

        taxAction.perform(mockPlayer);

        verify(mockGame, never()).payTax(mockPlayer, validAmount);
    }
}
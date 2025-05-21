package edu.ntnu.stud.boardgame.model.action;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.action.registry.MonopolyActionRegistry;
import edu.ntnu.stud.boardgame.model.game.MonopolyGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertyActionTest {

    private PropertyAction propertyAction;
    private final int validPrice = 100;
    private MonopolyGame mockGame;
    private MonopolyActionRegistry registry;

    @BeforeEach
    void setUp() {
        propertyAction = new PropertyAction(validPrice);
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
    void constructor_withValidPrice_shouldCreateInstance() {
        assertNotNull(propertyAction);
        assertEquals(validPrice, propertyAction.getPrice());
        assertNull(propertyAction.getOwner(), "Owner should be null initially.");
    }

    @Test
    void constructor_withNegativePrice_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new PropertyAction(-50);
        });
        assertEquals("Property price cannot be negative.", exception.getMessage());
    }

    @Test
    void getPrice_shouldReturnCorrectPrice() {
        assertEquals(validPrice, propertyAction.getPrice());
    }

    @Test
    void getOwner_initially_shouldReturnNull() {
        assertNull(propertyAction.getOwner());
    }

    @Test
    void setOwner_shouldUpdateOwner() {
        Player mockPlayer = mock(Player.class);
        propertyAction.setOwner(mockPlayer);
        assertEquals(mockPlayer, propertyAction.getOwner());
    }

    @Test
    void setOwner_withNull_shouldSetOwnerToNull() {
        Player mockPlayer = mock(Player.class);
        propertyAction.setOwner(mockPlayer);
        propertyAction.setOwner(null);
        assertNull(propertyAction.getOwner());
    }

    @Test
    void perform_withNullPlayer_shouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            propertyAction.perform(null);
        });
        assertEquals("Player cannot be null for PropertyAction.", exception.getMessage());
        verify(mockGame, never()).payRent(null, null, 0);
    }

    @Test
    void perform_withValidPlayer_shouldDelegateToMonopolyActionRegistry() {
        Player mockPlayer = mock(Player.class);
        Player mockOwner = mock(Player.class);
        propertyAction.setOwner(mockOwner);

        propertyAction.perform(mockPlayer);

        int expectedRent = validPrice / 5;
        verify(mockGame, times(1)).payRent(mockPlayer, mockOwner, expectedRent);
    }

    @Test
    void perform_whenPropertyHasNoOwner_shouldDelegateAndNotCallPayRent() {
        Player mockPlayer = mock(Player.class);
        propertyAction.setOwner(null);

        propertyAction.perform(mockPlayer);

        verify(mockGame, never()).payRent(mockPlayer, null, 0);
    }

    @Test
    void perform_whenPlayerIsOwner_shouldDelegateAndNotCallPayRent() {
        Player mockPlayer = mock(Player.class);
        propertyAction.setOwner(mockPlayer);

        propertyAction.perform(mockPlayer);

        verify(mockGame, never()).payRent(mockPlayer, mockPlayer, 0);
    }
}
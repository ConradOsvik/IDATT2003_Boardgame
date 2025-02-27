package edu.ntnu.stud.boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Dice}
 */
class DiceTest {
    private Dice dice;

    @BeforeEach
    void setup() {
        dice = new Dice(2);
    }

    @Test
    void roll_TwoDice_ReturnsSumBetween2And12() {
        int result = dice.roll();
        assertTrue(result >= 2 && result <= 12);
    }

    @Test
    void getDie_ValidIndex_ReturnsDieInstance() {
        assertNotNull(dice.getDie(0));
    }

    @Test
    void getDie_InvalidIndex_ThrowsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(2));
    }
}
package edu.ntnu.stud.boardgame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link Die}
 */
class DieTest {
    private Die die;

    @BeforeEach
    void setup() {
        die = new Die();
    }

    @Test
    void roll_NormalRoll_ReturnsValueBetween1And6() {
        int result = die.roll();
        assertTrue(result >= 1 && result <= 6);
    }

    @Test
    void getValue_BeforeRoll_ReturnsZero() {
        assertEquals(0, die.getValue());
    }
}
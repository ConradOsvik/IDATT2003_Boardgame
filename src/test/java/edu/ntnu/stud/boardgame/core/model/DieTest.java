package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Dice}
 */
class DieTest {

  private Die die;

  @BeforeEach
  void setup() {
    die = new Die();
  }

  @RepeatedTest(20)
  void roll_normalRoll_isBetweenOneAndSix() {
    int result = die.roll();

    assertTrue(result >= 1 && result <= 6);
  }

  @Test
  void roll_multipleRolls_updatesValue() {
    die.roll();
    int firstValue = die.getValue();

    int maxAttempts = 20;
    int newValue = firstValue;
    int attempts = 0;

    while (newValue == firstValue && attempts < maxAttempts) {
      newValue = die.roll();
      attempts++;
    }

    // In the extremely rare case that we got the same value 20 times in a row, the test will pass
    if (attempts < maxAttempts) {
      assertNotEquals(firstValue, die.getValue());
    }
  }

  @Test
  void getValue_beforeRoll_isZero() {
    assertEquals(0, die.getValue());
  }

  @Test
  void getValue_afterRoll_isEqualToRoll() {
    int result = die.roll();

    assertEquals(result, die.getValue());
  }
}

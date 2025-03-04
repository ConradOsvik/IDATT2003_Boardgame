package edu.ntnu.stud.boardgame.core.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

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
  void constructor_negativeNumberOfDice_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new Dice(-1));
  }

  @Test
  void constructor_zeroNumberOfDice_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> new Dice(0));
  }

  @Test
  void constructor_PositiveNumberOfDice_createsDice() {
    Dice dice = assertDoesNotThrow(() -> new Dice(1));
  }

  @RepeatedTest(20)
  void roll_normalRoll_isBetweenTwoAndTwelve() {
    int result = dice.roll();

    assertTrue(result >= 2 && result <= 12);
  }

  @Test
  void getDie_validIndex_returnsDieValue() {
    dice.roll();

    int result1 = dice.getDie(0);
    int result2 = dice.getDie(1);

    assertTrue(result1 >= 1 && result1 <= 6);
    assertTrue(result2 >= 1 && result2 <= 6);
  }

  @Test
  void getDie_invalidIndex_throwsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(2));
  }
}

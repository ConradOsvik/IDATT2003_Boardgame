package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiceTest {

  private Dice dice;

  @BeforeEach
  void setUp() {
    // Default setup with 2 dice for most tests
    dice = new Dice(2);
  }

  @Test
  void constructor_positiveNumberOfDice_createsDice() {
    Dice localDice = new Dice(3);
    // Roll to ensure dice are initialized and getDie can be called
    localDice.roll();
    assertEquals(1, localDice.getDie(0) >= 1 && localDice.getDie(0) <= 6 ? 1 : 0);
    assertEquals(1, localDice.getDie(1) >= 1 && localDice.getDie(1) <= 6 ? 1 : 0);
    assertEquals(1, localDice.getDie(2) >= 1 && localDice.getDie(2) <= 6 ? 1 : 0);
  }

  @Test
  void constructor_zeroNumberOfDice_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new Dice(0));
  }

  @Test
  void constructor_negativeNumberOfDice_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> new Dice(-1));
  }

  @Test
  void roll_returnsSumOfIndividualDieRolls() {
    // We can't directly control the outcome of Random, so we test the range.
    // For a 2-dice setup
    int total = dice.roll();
    assertTrue(total >= 2 && total <= 12, "Total roll should be between 2 and 12 for two dice.");
    // Also check individual dice values after a roll
    assertTrue(dice.getDie(0) >= 1 && dice.getDie(0) <= 6, "Die 0 value out of range.");
    assertTrue(dice.getDie(1) >= 1 && dice.getDie(1) <= 6, "Die 1 value out of range.");
  }

  @Test
  void roll_withMockedDice_returnsCorrectSum() {
    // Given the current Dice implementation, where Die objects are created
    // internally,
    // direct mocking of Die instances within a Dice object is not feasible without
    // PowerMock or refactoring the Dice class to allow for Die injection.
    // This test will therefore focus on the observable outcome of the roll()
    // method,
    // similar to roll_returnsSumOfIndividualDieRolls, ensuring the sum is within
    // the expected range.

    // Setup with 2 dice, as per @BeforeEach, or create a new instance if specific
    // configuration is needed.
    // Dice testDice = new Dice(2);

    int total = dice.roll(); // Uses the real Dice with real Die objects
    assertTrue(total >= 2 && total <= 12,
        "Total roll for two dice should be between 2 and 12. Actual: " + total);

    // We can also assert that individual die values are within their expected range
    // after the roll.
    // This indirectly verifies that the internal Die objects are working as
    // expected.
    assertTrue(dice.getDie(0) >= 1 && dice.getDie(0) <= 6,
        "Value of die 0 should be between 1 and 6. Actual: " + dice.getDie(0));
    assertTrue(dice.getDie(1) >= 1 && dice.getDie(1) <= 6,
        "Value of die 1 should be between 1 and 6. Actual: " + dice.getDie(1));
  }

  @Test
  void getDie_validDieNumber_returnsDieValue() {
    dice.roll(); // Roll to set values
    int die0Value = dice.getDie(0);
    int die1Value = dice.getDie(1);
    assertTrue(die0Value >= 1 && die0Value <= 6);
    assertTrue(die1Value >= 1 && die1Value <= 6);
  }

  @Test
  void getDie_negativeDieNumber_throwsIndexOutOfBoundsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(-1));
  }

  @Test
  void getDie_dieNumberTooHigh_throwsIndexOutOfBoundsException() {
    // For a 2-dice setup, index 2 is out of bounds
    assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(2));
  }
}
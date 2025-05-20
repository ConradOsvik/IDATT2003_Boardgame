package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Dice Tests")
class DiceTest {

  private Dice dice;

  @BeforeEach
  void setUp() {
    dice = new Dice(2);
  }

  @Nested
  @DisplayName("Roll Tests")
  class RollTests {

    @Test
    @DisplayName("Rolling with 2 dice should return value between 2 and 12")
    void roll_twoDice_returnsBetweenTwoAndTwelve() {
      int result = dice.roll();
      assertTrue(result >= 2 && result <= 12);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    @DisplayName("Rolling with n dice should return value between n and 6n")
    void roll_multipleNumberOfDice_returnsWithinExpectedRange(int dieCount) {
      Dice multipleDice = new Dice(dieCount);

      int result = multipleDice.roll();

      assertTrue(result >= dieCount && result <= dieCount * 6,
          "Roll with " + dieCount + " dice should be between " + dieCount + " and " + (dieCount
              * 6));
    }
  }

  @Nested
  @DisplayName("GetDie Tests")
  class GetDieTests {

    @Test
    @DisplayName("Getting die value after roll should return values between 1 and 6")
    void getDie_afterRoll_returnsValidDieValue() {
      dice.roll();

      int firstDie = dice.getDie(0);
      int secondDie = dice.getDie(1);

      assertTrue(firstDie >= 1 && firstDie <= 6);
      assertTrue(secondDie >= 1 && secondDie <= 6);
    }

    @Test
    @DisplayName("Getting die with negative index should throw exception")
    void getDie_negativeIndex_throwsException() {
      assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(-1));
    }

    @Test
    @DisplayName("Getting die with too large index should throw exception")
    void getDie_indexTooLarge_throwsException() {
      assertThrows(IndexOutOfBoundsException.class, () -> dice.getDie(2));
    }
  }
}
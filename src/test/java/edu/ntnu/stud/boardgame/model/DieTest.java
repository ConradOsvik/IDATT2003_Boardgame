package edu.ntnu.stud.boardgame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

@DisplayName("Die Tests")
class DieTest {

  private Die die;

  @BeforeEach
  void setUp() {
    die = new Die();
  }

  @Test
  @DisplayName("New die should have initial value of 0")
  void getValue_newDie_returnsZero() {
    assertEquals(0, die.getValue());
  }

  @RepeatedTest(100)
  @DisplayName("Rolling die should return value between 1 and 6")
  void roll_anyState_returnsBetweenOneAndSix() {
    int result = die.roll();
    assertTrue(result >= 1 && result <= 6);
  }

  @Test
  @DisplayName("getValue should return last rolled value")
  void getValue_afterRoll_returnsLastRolledValue() {
    int rolledValue = die.roll();
    assertEquals(rolledValue, die.getValue());
  }
}

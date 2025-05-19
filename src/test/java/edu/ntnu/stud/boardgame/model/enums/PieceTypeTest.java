package edu.ntnu.stud.boardgame.model.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("PieceType Tests")
class PieceTypeTest {

  @Nested
  @DisplayName("DisplayName Tests")
  class DisplayNameTests {

    @ParameterizedTest
    @EnumSource(PieceType.class)
    @DisplayName("getDisplayName should return non-empty value")
    void getDisplayName_anyPieceType_returnsNonEmptyValue(PieceType pieceType) {
      String displayName = pieceType.getDisplayName();

      assertNotNull(displayName);
      assertFalse(displayName.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
        "RED, RedToken",
        "BLUE, BlueToken",
        "GREEN, GreenToken",
        "YELLOW, YellowToken",
        "BLACK, BlackToken"
    })
    @DisplayName("getDisplayName should return correct display name")
    void getDisplayName_specificPieceType_returnsCorrectDisplayName(PieceType pieceType,
        String expectedName) {
      String displayName = pieceType.getDisplayName();

      assertEquals(expectedName, displayName);
    }
  }

  @Nested
  @DisplayName("FromDisplayName Tests")
  class FromDisplayNameTests {

    @ParameterizedTest
    @CsvSource({
        "RedToken, RED",
        "BlueToken, BLUE",
        "GreenToken, GREEN",
        "YellowToken, YELLOW",
        "BlackToken, BLACK"
    })
    @DisplayName("fromDisplayName should return correct piece type")
    void fromDisplayName_validDisplayName_returnsCorrectPieceType(String displayName,
        PieceType expectedType) {
      PieceType pieceType = PieceType.fromDisplayName(displayName);

      assertEquals(expectedType, pieceType);
    }

    @Test
    @DisplayName("fromDisplayName with invalid name should throw exception")
    void fromDisplayName_invalidDisplayName_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> {
        PieceType.fromDisplayName("NonExistentToken");
      });
    }

    @Test
    @DisplayName("fromDisplayName with null should throw exception")
    void fromDisplayName_nullDisplayName_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> {
        PieceType.fromDisplayName(null);
      });
    }

    @Test
    @DisplayName("fromDisplayName with empty string should throw exception")
    void fromDisplayName_emptyDisplayName_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> {
        PieceType.fromDisplayName("");
      });
    }

    @Test
    @DisplayName("fromDisplayName should be case insensitive")
    void fromDisplayName_differentCase_isCaseInsensitive() {
      PieceType pieceType = PieceType.fromDisplayName("redtoken");

      assertEquals(PieceType.RED, pieceType);
    }
  }

  @Nested
  @DisplayName("Formatting Tests")
  class FormattingTests {

    @ParameterizedTest
    @EnumSource(PieceType.class)
    @DisplayName("getFormattedDisplayName should return formatted string")
    void getFormattedDisplayName_anyPieceType_returnsFormattedString(PieceType pieceType) {
      String formatted = pieceType.getFormattedDisplayName();

      assertNotNull(formatted);
      assertFalse(formatted.isEmpty());
      assertTrue(formatted.contains(" "), "Should contain spaces for CamelCase separation");
    }

    @Test
    @DisplayName("toString should return formatted display name")
    void toString_anyPieceType_returnsFormattedDisplayName() {
      for (PieceType type : PieceType.values()) {
        String toString = type.toString();
        String formatted = type.getFormattedDisplayName();

        assertEquals(formatted, toString);
      }
    }
  }
}
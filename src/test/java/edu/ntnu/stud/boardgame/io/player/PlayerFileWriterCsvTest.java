package edu.ntnu.stud.boardgame.io.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import edu.ntnu.stud.boardgame.exception.files.PlayerWritingException;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerFileWriterCsvTest {

  private PlayerFileWriterCsv playerWriter;

  private Path mockPath;

  private List<Player> testPlayers;

  @BeforeEach
  void setUp() {
    playerWriter = new PlayerFileWriterCsv();
    mockPath = mock(Path.class);

    testPlayers = new ArrayList<>();
    testPlayers.add(new Player("Alice", PieceType.RED));
    testPlayers.add(new Player("Bob", PieceType.BLUE));
    testPlayers.add(new Player("Charlie", PieceType.GREEN));
  }

  @Test
  void writePlayers_validPlayersAndPath_writesCorrectCsv()
      throws PlayerWritingException, IOException {
    StringWriter stringWriter = new StringWriter();

    String expectedCsv =
        "Alice,RedToken"
            + System.lineSeparator()
            + "Bob,BlueToken"
            + System.lineSeparator()
            + "Charlie,GreenToken"
            + System.lineSeparator();

    try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
      mockedFiles
          .when(() -> Files.newBufferedWriter(mockPath))
          .thenReturn(new BufferedWriter(stringWriter));

      playerWriter.writePlayers(mockPath, testPlayers);

      assertEquals(expectedCsv, stringWriter.toString());
    }
  }

  @Test
  void writePlayers_nullPath_throwsPlayerWritingException() {
    assertThrows(PlayerWritingException.class, () -> playerWriter.writePlayers(null, testPlayers));
  }

  @Test
  void writePlayers_nullPlayerList_throwsPlayerWritingException() {
    PlayerWritingException ex =
        assertThrows(PlayerWritingException.class, () -> playerWriter.writePlayers(mockPath, null));
    assertEquals("Player list is null or empty", ex.getMessage());
  }

  @Test
  void writePlayers_emptyPlayerList_throwsPlayerWritingException() {
    List<Player> emptyList = new ArrayList<>();
    PlayerWritingException ex =
        assertThrows(
            PlayerWritingException.class, () -> playerWriter.writePlayers(mockPath, emptyList));
    assertEquals("Player list is null or empty", ex.getMessage());
  }

  @Test
  void writePlayers_ioExceptionDuringWrite_throwsPlayerWritingException() throws IOException {

    try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
      BufferedWriter mockBufferedWriter = mock(BufferedWriter.class);
      lenient()
          .doThrow(new IOException("Disk is full"))
          .when(mockBufferedWriter)
          .write(anyString());

      lenient().doThrow(new IOException("Disk is full")).when(mockBufferedWriter).newLine();

      mockedFiles.when(() -> Files.newBufferedWriter(mockPath)).thenReturn(mockBufferedWriter);

      PlayerWritingException ex =
          assertThrows(
              PlayerWritingException.class, () -> playerWriter.writePlayers(mockPath, testPlayers));
      assertTrue(ex.getMessage().contains("Failed to write players to file: Disk is full"));
    }
  }
}

package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerParsingException;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerFileReaderCsvTest {

    private PlayerFileReaderCsv playerReader;

    private Path mockPath;

    @BeforeEach
    void setUp() {
        playerReader = new PlayerFileReaderCsv();
        mockPath = mock(Path.class);
    }

    private StringReader createCsvReader(String csvData) {
        return new StringReader(csvData);
    }

    @Test
    void readPlayers_validCsv_parsesPlayersCorrectly() throws PlayerParsingException, IOException {
        String validCsv = "Alice, RedToken\nBob, BlueToken \n Charlie, GreenToken ";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(createCsvReader(validCsv)));

            List<Player> players = playerReader.readPlayers(mockPath);

            assertNotNull(players);
            assertEquals(3, players.size());

            assertEquals("Alice", players.get(0).getName());
            assertEquals(PieceType.RED, players.get(0).getPiece());

            assertEquals("Bob", players.get(1).getName());
            assertEquals(PieceType.BLUE, players.get(1).getPiece());

            assertEquals("Charlie", players.get(2).getName());
            assertEquals(PieceType.GREEN, players.get(2).getPiece());
        }
    }

    @Test
    void readPlayers_emptyLinesInCsv_areSkipped() throws PlayerParsingException, IOException {
        String csvWithEmptyLines = "Alice, RedToken\n\nBob, BlueToken\n   \nCharlie, GreenToken";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(createCsvReader(csvWithEmptyLines)));

            List<Player> players = playerReader.readPlayers(mockPath);
            assertEquals(3, players.size());
            assertEquals("Alice", players.get(0).getName());
            assertEquals(PieceType.RED, players.get(0).getPiece());
            assertEquals("Bob", players.get(1).getName());
            assertEquals(PieceType.BLUE, players.get(1).getPiece());
            assertEquals("Charlie", players.get(2).getName());
            assertEquals(PieceType.GREEN, players.get(2).getPiece());
        }
    }

    @Test
    void readPlayers_invalidCsvLineFormat_throwsPlayerParsingException() throws IOException {
        String invalidCsv = "Alice, RedToken\nBobBlueTokenCharlieGreenToken";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(createCsvReader(invalidCsv)));

            PlayerParsingException ex = assertThrows(PlayerParsingException.class,
                    () -> playerReader.readPlayers(mockPath));
            assertTrue(ex.getMessage().contains("Invalid player data at line 2"));
        }
    }

    @Test
    void readPlayers_emptyNameOrPiece_throwsPlayerParsingException() throws IOException {
        String invalidCsv = "Alice, RedToken\n, BlueToken";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(createCsvReader(invalidCsv)));

            PlayerParsingException ex = assertThrows(PlayerParsingException.class,
                    () -> playerReader.readPlayers(mockPath));
            assertTrue(ex.getMessage().contains("Invalid player data at line 2"));
        }
    }

    @Test
    void readPlayers_invalidPieceType_throwsPlayerParsingException() throws IOException {
        String invalidCsv = "Alice, RedToken\nBob, InvalidPieceName";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(createCsvReader(invalidCsv)));

            PlayerParsingException ex = assertThrows(PlayerParsingException.class,
                    () -> playerReader.readPlayers(mockPath));
            assertTrue(ex.getMessage().contains("Invalid piece type at line 2"));
        }
    }

    @Test
    void readPlayers_nullPath_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> playerReader.readPlayers(null));
    }

    @Test
    void readPlayers_ioExceptionDuringRead_throwsPlayerParsingException() throws IOException {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath)).thenThrow(new IOException("Disk read error"));

            PlayerParsingException ex = assertThrows(PlayerParsingException.class,
                    () -> playerReader.readPlayers(mockPath));
            assertTrue(ex.getMessage().contains("Failed to read players from file: Disk read error"));
        }
    }
}
package edu.ntnu.stud.boardgame.io.board;

import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BoardFileReaderGsonTest {

    private BoardFileReaderGson boardReader;

    @Mock
    private Path mockPath;

    @Mock
    private BasicFileAttributes mockAttributes;

    @BeforeEach
    void setUp() {
        boardReader = new BoardFileReaderGson();
        when(mockAttributes.isDirectory()).thenReturn(false);
    }

    @Test
    void readBoard_validJson_parsesBoardCorrectly() throws BoardParsingException, IOException {
        String validJson = "{\"name\":\"Test Board\", \"description\":\"A board for testing\", \"rows\":10, \"columns\":10, \"startTileId\":0, \"endTileId\":99, \"tiles\":[\n"
                +
                "  {\"id\":0, \"row\":9, \"column\":0, \"name\":\"Start\", \"action\":{\"type\":\"StartAction\", \"amount\":200}},\n"
                +
                "  {\"id\":1, \"row\":9, \"column\":1, \"name\":\"Property 1\", \"action\":{\"type\":\"PropertyAction\", \"price\":60}, \"nextTileId\":2},\n"
                +
                "  {\"id\":2, \"row\":9, \"column\":2, \"name\":\"Ladder Up\", \"action\":{\"type\":\"LadderAction\", \"destinationTileId\":5}},\n"
                +
                "  {\"id\":3, \"row\":9, \"column\":3, \"name\":\"Tax\", \"action\":{\"type\":\"TaxAction\", \"amount\":100}},\n"
                +
                "  {\"id\":4, \"row\":9, \"column\":4, \"name\":\"Snake Down\", \"action\":{\"type\":\"SnakeAction\", \"destinationTileId\":1}},\n"
                +
                "  {\"id\":5, \"row\":8, \"column\":2, \"name\":\"Top of Ladder\"},\n" +
                "  {\"id\":6, \"row\":9, \"column\":6, \"name\":\"Skip\", \"action\":{\"type\":\"SkipTurnAction\"}}\n" +
                "]}";

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(new StringReader(validJson)));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);

            Board board = boardReader.readBoard(mockPath);

            assertNotNull(board);
            assertEquals("Test Board", board.getName());
            assertEquals("A board for testing", board.getDescription());
            assertEquals(10, board.getRows());
            assertEquals(10, board.getColumns());
            assertEquals(0, board.getStartTileId());
            assertEquals(99, board.getEndTileId());
            assertEquals(7, board.getTiles().size());

            Tile tile0 = board.getTile(0);
            assertNotNull(tile0);
            assertEquals("Start", tile0.getName());
            assertTrue(tile0.getLandAction() instanceof StartAction);
            assertEquals(200, ((StartAction) tile0.getLandAction()).getAmount());

            Tile tile1 = board.getTile(1);
            assertNotNull(tile1);
            assertEquals("Property 1", tile1.getName());
            assertTrue(tile1.getLandAction() instanceof PropertyAction);
            assertEquals(60, ((PropertyAction) tile1.getLandAction()).getPrice());
            assertNotNull(tile1.getNextTile());
            assertEquals(2, tile1.getNextTile().getTileId());

            Tile tile2 = board.getTile(2);
            assertTrue(tile2.getLandAction() instanceof LadderAction);
            assertEquals(board.getTile(5), ((LadderAction) tile2.getLandAction()).getDestinationTile());

            Tile tile3 = board.getTile(3);
            assertTrue(tile3.getLandAction() instanceof TaxAction);
            assertEquals(100, ((TaxAction) tile3.getLandAction()).getAmount());

            Tile tile4 = board.getTile(4);
            assertTrue(tile4.getLandAction() instanceof SnakeAction);
            assertEquals(board.getTile(1), ((SnakeAction) tile4.getLandAction()).getDestinationTile());

            Tile tile6 = board.getTile(6);
            assertTrue(tile6.getLandAction() instanceof SkipTurnAction);
        }
    }

    @Test
    void readBoard_nullPath_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> boardReader.readBoard(null));
    }

    @Test
    void readBoard_ioException_throwsBoardParsingException() throws IOException {
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath)).thenThrow(new IOException("Disk error"));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            BoardParsingException ex = assertThrows(BoardParsingException.class, () -> boardReader.readBoard(mockPath));
            assertTrue(ex.getMessage().contains("Failed to read board file: Disk error"));
        }
    }

    @Test
    void readBoard_invalidJsonSyntax_throwsBoardParsingException() throws IOException {
        String invalidJson = "{\"name\": \"Test Board\", ...";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(new StringReader(invalidJson)));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            BoardParsingException ex = assertThrows(BoardParsingException.class, () -> boardReader.readBoard(mockPath));
            assertTrue(ex.getMessage().contains("Invalid JSON syntax"));
        }
    }

    @Test
    void readBoard_missingBoardField_throwsBoardParsingException() throws IOException {
        String json = "{\"description\":\"A board for testing\", \"rows\":10, \"columns\":10, \"tiles\":[]}";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(new StringReader(json)));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            BoardParsingException ex = assertThrows(BoardParsingException.class, () -> boardReader.readBoard(mockPath));
            assertEquals("Board must have name, description, rows, and columns", ex.getMessage());
        }
    }

    @Test
    void readBoard_missingTileId_throwsBoardParsingException() throws IOException {
        String json = "{\"name\":\"Test\",\"description\":\"Desc\",\"rows\":1,\"columns\":1,\"startTileId\":0,\"endTileId\":0,\"tiles\":[{\"row\":0,\"column\":0}]}";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(new StringReader(json)));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            BoardParsingException ex = assertThrows(BoardParsingException.class, () -> boardReader.readBoard(mockPath));
            assertEquals("Tile must have id, row, and column", ex.getMessage());
        }
    }

    @Test
    void readBoard_missingActionType_throwsBoardParsingException() throws IOException {
        String json = "{\"name\":\"T\",\"description\":\"D\",\"rows\":1,\"columns\":1,\"startTileId\":0,\"endTileId\":0,\"tiles\":[{\"id\":0,\"action\":{\"destinationTileId\":0}}]}";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(new StringReader(json)));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            BoardParsingException ex = assertThrows(BoardParsingException.class, () -> boardReader.readBoard(mockPath));
            assertEquals("Action must have type", ex.getMessage());
        }
    }

    @Test
    void readBoard_missingTilesArray_throwsBoardParsingException() throws IOException {
        String json = "{\"name\":\"Test\",\"description\":\"Desc\",\"rows\":1,\"columns\":1,\"startTileId\":0,\"endTileId\":0}";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(new StringReader(json)));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            BoardParsingException ex = assertThrows(BoardParsingException.class, () -> boardReader.readBoard(mockPath));
            assertEquals("Board must have a 'tiles' array", ex.getMessage());
        }
    }

    @Test
    void readBoard_emptyTilesArray_parsesSuccessfully() throws IOException, BoardParsingException {
        String json = "{\"name\":\"Test\",\"description\":\"Desc\",\"rows\":1,\"columns\":1,\"startTileId\":0,\"endTileId\":0,\"tiles\":[]}";
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.newBufferedReader(mockPath))
                    .thenReturn(new BufferedReader(new StringReader(json)));
            mockedFiles.when(() -> Files.readAttributes(any(Path.class), eq(BasicFileAttributes.class)))
                    .thenReturn(mockAttributes);
            mockedFiles.when(() -> Files.exists(any(Path.class))).thenReturn(true);
            Board board = boardReader.readBoard(mockPath);
            assertNotNull(board);
            assertEquals(0, board.getTiles().size());
        }
    }
}
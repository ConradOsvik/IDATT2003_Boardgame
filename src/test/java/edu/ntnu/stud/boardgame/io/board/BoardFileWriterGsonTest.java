package edu.ntnu.stud.boardgame.io.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardFileWriterGsonTest {

    private BoardFileWriterGson boardWriter;
    private Gson gson;

    private Path mockPath;
    private File mockFile;

    private Board testBoard;

    @BeforeEach
    void setUp() {
        boardWriter = new BoardFileWriterGson();
        mockPath = mock(Path.class);
        mockFile = mock(File.class);
        gson = new GsonBuilder().create();

        lenient().when(mockPath.toFile()).thenReturn(mockFile);

        // Setup a detailed test board
        testBoard = new Board("Test Board", "A test description", 5, 5, 0, 24);
        Tile tile0 = new Tile(0); // Start
        tile0.setName("Start");
        tile0.setRow(4);
        tile0.setColumn(0);
        tile0.setLandAction(new StartAction(200));
        testBoard.addTile(tile0);

        Tile tile1 = new Tile(1);
        tile1.setName("Property1");
        tile1.setRow(4);
        tile1.setColumn(1);
        tile1.setLandAction(new PropertyAction(60));
        testBoard.addTile(tile1);

        Tile tile2 = new Tile(2);
        tile2.setName("LadderTile");
        tile2.setRow(4);
        tile2.setColumn(2);
        Tile tile5 = new Tile(5);
        tile5.setName("LadderTop");
        tile5.setRow(3);
        tile5.setColumn(2);
        testBoard.addTile(tile5); // Add destination first
        tile2.setLandAction(new LadderAction(tile5));
        testBoard.addTile(tile2);

        Tile tile3 = new Tile(3);
        tile3.setName("SnakeTile");
        tile3.setRow(4);
        tile3.setColumn(3);
        Tile tile0Ref = testBoard.getTile(0); // Destination for snake
        tile3.setLandAction(new SnakeAction(tile0Ref));
        testBoard.addTile(tile3);

        Tile tile4 = new Tile(4);
        tile4.setName("TaxTile");
        tile4.setRow(4);
        tile4.setColumn(4);
        tile4.setLandAction(new TaxAction(100));
        testBoard.addTile(tile4);

        Tile tile6 = new Tile(6); // Skip Turn
        tile6.setName("Skip");
        tile6.setRow(3);
        tile6.setColumn(1);
        tile6.setLandAction(new SkipTurnAction());
        testBoard.addTile(tile6);

        // Link tiles (simplified for this test focus on writer)
        tile0.setNextTile(tile1);
        tile1.setNextTile(tile2);
        tile2.setNextTile(tile3);
        tile3.setNextTile(tile4);
        // ... and so on for a full board, not strictly necessary for writer logic test
    }

    @Test
    void writeBoard_validBoardAndPath_writesCorrectJson() throws IOException, BoardWritingException {
        StringWriter stringWriter = new StringWriter();

        try (MockedConstruction<FileWriter> mockedConstruction = Mockito.mockConstruction(FileWriter.class,
                (mock, context) -> {
                    // Ensure the mock FileWriter uses our StringWriter
                    doAnswer(invocation -> {
                        stringWriter.write((int) invocation.getArgument(0));
                        return null;
                    }).when(mock).write(anyInt());

                    doAnswer(invocation -> {
                        stringWriter.write((char[]) invocation.getArgument(0));
                        return null;
                    }).when(mock).write(any(char[].class));

                    doAnswer(invocation -> {
                        // This mock is for write(char[], int, int)
                        stringWriter.write((char[]) invocation.getArgument(0), invocation.getArgument(1),
                                invocation.getArgument(2));
                        return null;
                    }).when(mock).write(any(char[].class), anyInt(), anyInt());

                    doAnswer(invocation -> {
                        Object arg = invocation.getArgument(0);
                        if (arg instanceof String) {
                            stringWriter.write((String) arg);
                        }
                        return null;
                    }).when(mock).write(anyString());

                    // Adding mock for write(String, int, int)
                    doAnswer(invocation -> {
                        stringWriter.write((String) invocation.getArgument(0), invocation.getArgument(1),
                                invocation.getArgument(2));
                        return null;
                    }).when(mock).write(anyString(), anyInt(), anyInt());

                    doAnswer(invocation -> {
                        stringWriter.append(invocation.getArgument(0));
                        return mock;
                    }).when(mock).append(any(CharSequence.class));

                    doAnswer(invocation -> {
                        stringWriter.append(invocation.getArgument(0), invocation.getArgument(1),
                                invocation.getArgument(2));
                        return mock;
                    }).when(mock).append(any(CharSequence.class), anyInt(), anyInt());

                    doAnswer(invocation -> {
                        stringWriter.append((char) invocation.getArgument(0));
                        return mock;
                    }).when(mock).append(anyChar());

                    doAnswer(invocation -> {
                        stringWriter.flush();
                        return null; // void method
                    }).when(mock).flush();

                    doAnswer(invocation -> {
                        stringWriter.close();
                        return null;
                    }).when(mock).close();
                })) {

            boardWriter.writeBoard(mockPath, testBoard);

            // Verify FileWriter was constructed with the mockFile
            assertEquals(1, mockedConstruction.constructed().size());
            FileWriter constructedWriter = mockedConstruction.constructed().get(0);
            // We can't easily verify the file argument of the constructor without more
            // complex context matching
            // but we know it was called.

            String expectedJson = "{\"name\":\"Test Board\",\"description\":\"A test description\",\"rows\":5,\"columns\":5,\"startTileId\":0,\"endTileId\":24,\"tiles\":["
                    +
                    "{\"id\":0,\"row\":4,\"column\":0,\"name\":\"Start\",\"nextTileId\":1,\"action\":{\"type\":\"StartAction\",\"amount\":200,\"description\":\"Start tile with amount: 200\"}},"
                    +
                    "{\"id\":1,\"row\":4,\"column\":1,\"name\":\"Property1\",\"nextTileId\":2,\"action\":{\"type\":\"PropertyAction\",\"price\":60,\"description\":\"Property with price: 60\"}},"
                    +
                    "{\"id\":2,\"row\":4,\"column\":2,\"name\":\"LadderTile\",\"nextTileId\":3,\"action\":{\"type\":\"LadderAction\",\"destinationTileId\":5,\"description\":\"Ladder from 2 to 5\"}},"
                    +
                    "{\"id\":3,\"row\":4,\"column\":3,\"name\":\"SnakeTile\",\"nextTileId\":4,\"action\":{\"type\":\"SnakeAction\",\"destinationTileId\":0,\"description\":\"Snake from 3 to 0\"}},"
                    +
                    "{\"id\":4,\"row\":4,\"column\":4,\"name\":\"TaxTile\",\"action\":{\"type\":\"TaxAction\",\"amount\":100,\"description\":\"Tax with amount: 100\"}},"
                    +
                    "{\"id\":5,\"row\":3,\"column\":2,\"name\":\"LadderTop\"}," +
                    "{\"id\":6,\"row\":3,\"column\":1,\"name\":\"Skip\",\"action\":{\"type\":\"SkipTurnAction\",\"description\":\"Skip turn for player\"}}]}";

            JsonElement actualJsonElement = JsonParser.parseString(stringWriter.toString());
            JsonElement expectedJsonElement = JsonParser.parseString(expectedJson);

            assertEquals(expectedJsonElement, actualJsonElement);
        }
    }

    @Test
    void writeBoard_nullPath_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> boardWriter.writeBoard(null, testBoard));
    }

    @Test
    void writeBoard_nullBoard_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> boardWriter.writeBoard(mockPath, null));
    }

    @Test
    void writeBoard_ioExceptionDuringWrite_throwsBoardWritingException() throws IOException {
        try (MockedConstruction<FileWriter> mockedConstruction = Mockito.mockConstruction(FileWriter.class,
                (mock, context) -> {
                    // Throw IOException when FileWriter is closed
                    doThrow(new IOException("Disk full")).when(mock).close();
                })) {

            BoardWritingException ex = assertThrows(BoardWritingException.class,
                    () -> boardWriter.writeBoard(mockPath, testBoard));
            assertTrue(ex.getMessage().contains("Failed to write board file: Disk full"));
            assertEquals(1, mockedConstruction.constructed().size());
            FileWriter constructedWriter = mockedConstruction.constructed().get(0);
            verify(constructedWriter).close();
        }
    }
}
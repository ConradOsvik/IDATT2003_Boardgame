package edu.ntnu.stud.boardgame.io.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import edu.ntnu.stud.boardgame.model.action.StartAction;
import edu.ntnu.stud.boardgame.model.action.TaxAction;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for board parsing and serialization logic. These tests focus on the parsing logic
 * without file I/O operations.
 */
class BoardFileReaderWriterTest {

  private BoardFileReaderGson reader;
  private BoardFileWriterGson writer;

  @BeforeEach
  void setUp() {
    reader = new BoardFileReaderGson();
    writer = new BoardFileWriterGson();
  }

  @Test
  @DisplayName("Should parse valid basic board JSON")
  void shouldParseValidBasicBoard() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Test Board",
            "description": "A test board",
            "rows": 3,
            "columns": 3,
            "startTileId": 0,
            "endTileId": 9,
            "tiles": [
                {
                    "id": 1,
                    "row": 0,
                    "column": 0,
                    "nextTileId": 2
                },
                {
                    "id": 2,
                    "row": 0,
                    "column": 1
                }
            ]
        }
        """;

    // Act
    Board board = reader.parseBoard(json);

    // Assert
    assertNotNull(board);
    assertEquals("Test Board", board.getName());
    assertEquals("A test board", board.getDescription());
    assertEquals(3, board.getRows());
    assertEquals(3, board.getColumns());
    assertEquals(0, board.getStartTileId());
    assertEquals(9, board.getEndTileId());

    Tile tile1 = board.getTile(1);
    assertNotNull(tile1);
    assertEquals(1, tile1.getTileId());
    assertEquals(0, tile1.getRow());
    assertEquals(0, tile1.getColumn());

    Tile tile2 = board.getTile(2);
    assertNotNull(tile2);
    assertEquals(tile2, tile1.getNextTile());
  }

  @Test
  @DisplayName("Should parse board with ladder action")
  void shouldParseBoardWithLadderAction() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Ladder Board",
            "description": "Board with ladder",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4,
            "tiles": [
                {
                    "id": 1,
                    "row": 1,
                    "column": 0,
                    "action": {
                        "type": "LadderAction",
                        "destinationTileId": 3,
                        "description": "Ladder from 1 to 3"
                    }
                },
                {
                    "id": 3,
                    "row": 0,
                    "column": 1
                }
            ]
        }
        """;

    // Act
    Board board = reader.parseBoard(json);

    // Assert
    Tile tile1 = board.getTile(1);
    assertNotNull(tile1.getLandAction());
    assertTrue(tile1.getLandAction() instanceof LadderAction);

    LadderAction ladder = (LadderAction) tile1.getLandAction();
    assertEquals(3, ladder.getDestinationTile().getTileId());
  }

  @Test
  @DisplayName("Should parse board with snake action")
  void shouldParseBoardWithSnakeAction() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Snake Board",
            "description": "Board with snake",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4,
            "tiles": [
                {
                    "id": 3,
                    "row": 0,
                    "column": 1,
                    "action": {
                        "type": "SnakeAction",
                        "destinationTileId": 1,
                        "description": "Snake from 3 to 1"
                    }
                },
                {
                    "id": 1,
                    "row": 1,
                    "column": 0
                }
            ]
        }
        """;

    // Act
    Board board = reader.parseBoard(json);

    // Assert
    Tile tile3 = board.getTile(3);
    assertNotNull(tile3.getLandAction());
    assertTrue(tile3.getLandAction() instanceof SnakeAction);

    SnakeAction snake = (SnakeAction) tile3.getLandAction();
    assertEquals(1, snake.getDestinationTile().getTileId());
  }

  @Test
  @DisplayName("Should parse board with property action")
  void shouldParseBoardWithPropertyAction() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Monopoly Board",
            "description": "Board with property",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4,
            "tiles": [
                {
                    "id": 1,
                    "row": 1,
                    "column": 0,
                    "action": {
                        "type": "PropertyAction",
                        "price": 200,
                        "description": "Property with price: 200"
                    }
                }
            ]
        }
        """;

    // Act
    Board board = reader.parseBoard(json);

    // Assert
    Tile tile1 = board.getTile(1);
    assertNotNull(tile1.getLandAction());
    assertTrue(tile1.getLandAction() instanceof PropertyAction);

    PropertyAction property = (PropertyAction) tile1.getLandAction();
    assertEquals(200, property.getPrice());
  }

  @Test
  @DisplayName("Should parse board with tax action")
  void shouldParseBoardWithTaxAction() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Tax Board",
            "description": "Board with tax",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4,
            "tiles": [
                {
                    "id": 1,
                    "row": 1,
                    "column": 0,
                    "action": {
                        "type": "TaxAction",
                        "amount": 100,
                        "description": "Tax with amount: 100"
                    }
                }
            ]
        }
        """;

    // Act
    Board board = reader.parseBoard(json);

    // Assert
    Tile tile1 = board.getTile(1);
    assertNotNull(tile1.getLandAction());
    assertTrue(tile1.getLandAction() instanceof TaxAction);

    TaxAction tax = (TaxAction) tile1.getLandAction();
    assertEquals(100, tax.getAmount());
  }

  @Test
  @DisplayName("Should parse board with start action")
  void shouldParseBoardWithStartAction() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Start Board",
            "description": "Board with start",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4,
            "tiles": [
                {
                    "id": 0,
                    "row": 1,
                    "column": 0,
                    "action": {
                        "type": "StartAction",
                        "amount": 50,
                        "description": "Start tile with amount: 50"
                    }
                }
            ]
        }
        """;

    // Act
    Board board = reader.parseBoard(json);

    // Assert
    Tile tile0 = board.getTile(0);
    assertNotNull(tile0.getLandAction());
    assertTrue(tile0.getLandAction() instanceof StartAction);

    StartAction start = (StartAction) tile0.getLandAction();
    assertEquals(50, start.getAmount());
  }

  @Test
  @DisplayName("Should parse board with skip turn action")
  void shouldParseBoardWithSkipTurnAction() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Skip Board",
            "description": "Board with skip turn",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4,
            "tiles": [
                {
                    "id": 1,
                    "row": 1,
                    "column": 0,
                    "action": {
                        "type": "SkipTurnAction",
                        "description": "Skip turn for player"
                    }
                }
            ]
        }
        """;

    // Act
    Board board = reader.parseBoard(json);

    // Assert
    Tile tile1 = board.getTile(1);
    assertNotNull(tile1.getLandAction());
    assertTrue(tile1.getLandAction() instanceof SkipTurnAction);
  }

  @Test
  @DisplayName("Should throw exception for missing required board properties")
  void shouldThrowExceptionForMissingBoardProperties() {
    // Arrange
    String invalidJson = """
        {
            "name": "Incomplete Board"
        }
        """;

    // Act & Assert
    BoardParsingException exception = assertThrows(
        BoardParsingException.class,
        () -> reader.parseBoard(invalidJson)
    );

    assertTrue(
        exception.getMessage().contains("Board must have name, description, rows, and columns"));
  }

  @Test
  @DisplayName("Should throw exception for missing tiles array")
  void shouldThrowExceptionForMissingTiles() {
    // Arrange
    String invalidJson = """
        {
            "name": "No Tiles Board",
            "description": "Board without tiles",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4
        }
        """;

    // Act & Assert
    BoardParsingException exception = assertThrows(
        BoardParsingException.class,
        () -> reader.parseBoard(invalidJson)
    );

    assertTrue(exception.getMessage().contains("Board must have a 'tiles' array"));
  }

  @Test
  @DisplayName("Should throw exception for unknown action type")
  void shouldThrowExceptionForUnknownActionType() {
    // Arrange
    String invalidJson = """
        {
            "name": "Invalid Action Board",
            "description": "Board with invalid action",
            "rows": 2,
            "columns": 2,
            "startTileId": 0,
            "endTileId": 4,
            "tiles": [
                {
                    "id": 1,
                    "row": 1,
                    "column": 0,
                    "action": {
                        "type": "UnknownAction"
                    }
                }
            ]
        }
        """;

    // Act & Assert
    BoardParsingException exception = assertThrows(
        BoardParsingException.class,
        () -> reader.parseBoard(invalidJson)
    );

    assertTrue(exception.getMessage().contains("Unknown action type: UnknownAction"));
  }

  @Test
  @DisplayName("Should serialize board to valid JSON")
  void shouldSerializeBoardToValidJson() throws BoardWritingException {
    // Arrange
    Board board = new Board("Test Board", "A test board", 2, 2, 0, 3);

    Tile tile1 = new Tile(1);
    tile1.setRow(1);
    tile1.setColumn(0);
    board.addTile(tile1);

    Tile tile2 = new Tile(2);
    tile2.setRow(1);
    tile2.setColumn(1);
    tile2.setLandAction(new PropertyAction(150));
    board.addTile(tile2);

    // Act
    String json = writer.serializeBoardToString(board);

    // Assert
    assertNotNull(json);
    assertFalse(json.trim().isEmpty());

    // Parse the JSON to verify it's valid
    JsonObject parsed = JsonParser.parseString(json).getAsJsonObject();
    assertEquals("Test Board", parsed.get("name").getAsString());
    assertEquals("A test board", parsed.get("description").getAsString());
    assertEquals(2, parsed.get("rows").getAsInt());
    assertEquals(2, parsed.get("columns").getAsInt());
    assertTrue(parsed.has("tiles"));
  }

  @Test
  @DisplayName("Should serialize and parse round-trip successfully")
  void shouldSerializeAndParseRoundTrip() throws BoardWritingException, BoardParsingException {
    // Arrange - Create a board with various actions
    Board originalBoard = new Board("Round Trip Board", "Test round trip", 3, 3, 0, 8);

    Tile tile1 = new Tile(1);
    tile1.setRow(2);
    tile1.setColumn(0);
    tile1.setLandAction(new PropertyAction(200));
    originalBoard.addTile(tile1);

    Tile tile2 = new Tile(2);
    tile2.setRow(2);
    tile2.setColumn(1);
    tile2.setLandAction(new TaxAction(50));
    originalBoard.addTile(tile2);

    Tile tile3 = new Tile(3);
    tile3.setRow(2);
    tile3.setColumn(2);
    tile3.setLandAction(new LadderAction(tile1)); // Points to tile1
    originalBoard.addTile(tile3);

    // Act - Serialize then parse
    String json = writer.serializeBoardToString(originalBoard);
    Board parsedBoard = reader.parseBoard(json);

    // Assert - Verify the round trip preserved the data
    assertEquals(originalBoard.getName(), parsedBoard.getName());
    assertEquals(originalBoard.getDescription(), parsedBoard.getDescription());
    assertEquals(originalBoard.getRows(), parsedBoard.getRows());
    assertEquals(originalBoard.getColumns(), parsedBoard.getColumns());

    // Check tiles and actions
    Tile parsedTile1 = parsedBoard.getTile(1);
    assertNotNull(parsedTile1);
    assertTrue(parsedTile1.getLandAction() instanceof PropertyAction);
    assertEquals(200, ((PropertyAction) parsedTile1.getLandAction()).getPrice());

    Tile parsedTile2 = parsedBoard.getTile(2);
    assertNotNull(parsedTile2);
    assertTrue(parsedTile2.getLandAction() instanceof TaxAction);
    assertEquals(50, ((TaxAction) parsedTile2.getLandAction()).getAmount());
  }

  @Test
  @DisplayName("Should use StringReader for parsing")
  void shouldUseStringReaderForParsing() throws BoardParsingException {
    // Arrange
    String json = """
        {
            "name": "Reader Test",
            "description": "Test with StringReader",
            "rows": 1,
            "columns": 1,
            "startTileId": 0,
            "endTileId": 1,
            "tiles": []
        }
        """;
    StringReader stringReader = new StringReader(json);

    // Act
    Board board = reader.parseBoard(stringReader);

    // Assert
    assertNotNull(board);
    assertEquals("Reader Test", board.getName());
  }

  @Test
  @DisplayName("Should use StringWriter for serialization")
  void shouldUseStringWriterForSerialization() throws BoardWritingException {
    // Arrange
    Board board = new Board("Writer Test", "Test with StringWriter", 1, 1, 0, 1);
    StringWriter stringWriter = new StringWriter();

    // Act
    writer.serializeBoard(stringWriter, board);

    // Assert
    String json = stringWriter.toString();
    assertNotNull(json);
    assertFalse(json.trim().isEmpty());
    assertTrue(json.contains("Writer Test"));
  }
}
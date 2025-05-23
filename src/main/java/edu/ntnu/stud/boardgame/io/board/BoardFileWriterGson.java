package edu.ntnu.stud.boardgame.io.board;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import edu.ntnu.stud.boardgame.model.action.StartAction;
import edu.ntnu.stud.boardgame.model.action.TaxAction;
import edu.ntnu.stud.boardgame.model.action.TileAction;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Implementation of {@link BoardFileWriter} that uses Google's Gson library for JSON
 * serialization.
 *
 * <p>This class provides functionality to serialize {@link Board} objects into JSON format
 * and save them to the filesystem. It handles the conversion of complex board structures, including
 * tiles and their associated actions, into a structured JSON representation.</p>
 *
 * <p>The writer handles different types of tile actions by converting them to appropriate
 * JSON objects with type information and relevant properties.</p>
 *
 * @see BoardFileWriter
 * @see Board
 * @see TileAction
 */
public class BoardFileWriterGson implements BoardFileWriter {

  private final Gson gson;

  /**
   * Constructs a new BoardFileWriterGson with a default Gson instance.
   */
  public BoardFileWriterGson() {
    this.gson = new Gson();
  }

  private static JsonObject getActionObject(Tile tile, TileAction action)
      throws BoardWritingException {
    JsonObject actionObject = new JsonObject();

    switch (action) {
      case LadderAction ladderAction -> {
        actionObject.addProperty("type", "LadderAction");
        actionObject.addProperty("destinationTileId",
            ladderAction.getDestinationTile().getTileId());
        actionObject.addProperty("description",
            "Ladder from " + tile.getTileId() + " to " + ladderAction.getDestinationTile()
                .getTileId());
      }
      case SnakeAction snakeAction -> {
        actionObject.addProperty("type", "SnakeAction");
        actionObject.addProperty("destinationTileId", snakeAction.getDestinationTile().getTileId());
        actionObject.addProperty("description",
            "Snake from " + tile.getTileId() + " to " + snakeAction.getDestinationTile()
                .getTileId());
      }
      case SkipTurnAction skipTurnAction -> {
        actionObject.addProperty("type", "SkipTurnAction");
        actionObject.addProperty("description", "Skip turn for player");
      }
      case PropertyAction propertyAction -> {
        actionObject.addProperty("type", "PropertyAction");
        actionObject.addProperty("price", propertyAction.getPrice());
        actionObject.addProperty("description",
            "Property with price: " + propertyAction.getPrice());
      }
      case TaxAction taxAction -> {
        actionObject.addProperty("type", "TaxAction");
        actionObject.addProperty("amount", taxAction.getAmount());
        actionObject.addProperty("description", "Tax with amount: " + taxAction.getAmount());
      }
      case StartAction startAction -> {
        actionObject.addProperty("type", "StartAction");
        actionObject.addProperty("amount", startAction.getAmount());
        actionObject.addProperty("description",
            "Start tile with amount: " + startAction.getAmount());
      }
      default -> throw new BoardWritingException("Unknown action type: " + action.getClass());
    }
    return actionObject;
  }

  private static JsonObject getTileObject(Tile tile) {
    JsonObject tileObject = new JsonObject();

    tileObject.addProperty("id", tile.getTileId());

    if (tile.getRow() != null) {
      tileObject.addProperty("row", tile.getRow());
    }

    if (tile.getColumn() != null) {
      tileObject.addProperty("column", tile.getColumn());
    }

    if (tile.getName() != null) {
      tileObject.addProperty("name", tile.getName());
    }

    if (tile.getNextTile() != null) {
      tileObject.addProperty("nextTileId", tile.getNextTile().getTileId());
    }
    return tileObject;
  }

  /**
   * Writes a board object to the specified file path in JSON format. This method handles file I/O
   * and delegates serialization to serializeBoard().
   */
  @Override
  public void writeBoard(Path path, Board board) throws BoardWritingException {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null.");
    }
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null.");
    }

    try (FileWriter writer = new FileWriter(path.toFile())) {
      serializeBoard(writer, board);
    } catch (IOException e) {
      throw new BoardWritingException("Failed to write board file: " + e.getMessage(), e);
    }
  }

  /**
   * Serializes a board object to a Writer. This method contains pure serialization logic and can be
   * tested without file I/O.
   *
   * @param writer the writer to output JSON to
   * @param board  the board object to serialize
   * @throws BoardWritingException if serialization fails
   */
  public void serializeBoard(Writer writer, Board board) throws BoardWritingException {
    if (writer == null) {
      throw new IllegalArgumentException("Writer cannot be null.");
    }
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null.");
    }

    try {
      JsonObject boardJson = createBoardJson(board);
      gson.toJson(boardJson, writer);
    } catch (Exception e) {
      throw new BoardWritingException("Unexpected error during serialization: " + e.getMessage(),
          e);
    }
  }

  /**
   * Serializes a board object to a JSON string. Convenience method for testing with string output.
   *
   * @param board the board object to serialize
   * @return JSON string representation of the board
   * @throws BoardWritingException if serialization fails
   */
  public String serializeBoardToString(Board board) throws BoardWritingException {
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null.");
    }

    try {
      JsonObject boardJson = createBoardJson(board);
      return gson.toJson(boardJson);
    } catch (Exception e) {
      throw new BoardWritingException("Unexpected error during serialization: " + e.getMessage(),
          e);
    }
  }

  /**
   * Creates the JSON object representation of a board. This method contains the core serialization
   * logic.
   */
  private JsonObject createBoardJson(Board board) throws BoardWritingException {
    JsonObject boardJson = new JsonObject();

    boardJson.addProperty("name", board.getName());
    boardJson.addProperty("description", board.getDescription());
    boardJson.addProperty("rows", board.getRows());
    boardJson.addProperty("columns", board.getColumns());
    boardJson.addProperty("startTileId", board.getStartTileId());
    boardJson.addProperty("endTileId", board.getEndTileId());

    JsonArray tilesArray = new JsonArray();
    Collection<Tile> tiles = board.getTiles().values();

    for (Tile tile : tiles) {
      JsonObject tileObject = getTileObject(tile);

      TileAction action = tile.getLandAction();
      if (action != null) {
        JsonObject actionObject = getActionObject(tile, action);
        tileObject.add("action", actionObject);
      }

      tilesArray.add(tileObject);
    }

    boardJson.add("tiles", tilesArray);
    return boardJson;
  }
}
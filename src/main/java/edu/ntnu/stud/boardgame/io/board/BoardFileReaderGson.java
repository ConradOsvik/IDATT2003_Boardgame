package edu.ntnu.stud.boardgame.io.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import edu.ntnu.stud.boardgame.exception.files.BoardParsingException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.PropertyAction;
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import edu.ntnu.stud.boardgame.model.action.StartAction;
import edu.ntnu.stud.boardgame.model.action.TaxAction;
import edu.ntnu.stud.boardgame.model.action.TileAction;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementation of {@link BoardFileReader} that reads board configurations from JSON files using
 * the Gson library.
 *
 * <p>This class parses JSON formatted board files and converts them into {@link Board} objects with
 * appropriate tiles and actions. The expected JSON structure includes board properties (name,
 * description, dimensions) and an array of tiles with their respective properties and actions.
 *
 * <p>Supported tile actions include:
 *
 * <ul>
 *   <li>LadderAction - moves player to a specified destination tile
 *   <li>SnakeAction - moves player to a specified destination tile
 *   <li>SkipTurnAction - causes player to skip their next turn
 *   <li>PropertyAction - represents purchasable property with a price
 *   <li>TaxAction - charges player a specified amount
 *   <li>StartAction - awards player a specified amount
 * </ul>
 *
 * @see BoardFileReader
 * @see Board
 * @see Tile
 * @see TileAction
 */
public class BoardFileReaderGson implements BoardFileReader {

  private static final String AMOUNT_PROPERTY = "amount";
  private static final String PRICE_PROPERTY = "price";
  private static final String TYPE_PROPERTY = "type";
  private static final String DESTINATION_TILE_ID_PROPERTY = "destinationTileId";
  private static final String LADDER_ACTION = "LadderAction";
  private static final String SNAKE_ACTION = "SnakeAction";

  private final Gson gson;

  /** Creates a new BoardFileReaderGson. */
  public BoardFileReaderGson() {
    this.gson = new GsonBuilder().create();
  }

  /**
   * Reads a board configuration from a JSON file at the specified path.
   *
   * <p>The method parses the JSON file, extracts board properties, creates tiles, and sets up their
   * connections and actions according to the JSON specification.
   *
   * @param path the file path to the JSON board configuration
   * @return a fully initialized {@link Board} object with all tiles and actions
   * @throws BoardParsingException if the file cannot be read, contains invalid JSON, or the board
   *     configuration is incomplete or invalid
   * @throws IllegalArgumentException if the path is null
   */
  @Override
  public Board readBoard(Path path) throws BoardParsingException {
    validatePath(path);

    try (Reader reader = Files.newBufferedReader(path)) {
      JsonObject boardJson = JsonParser.parseReader(reader).getAsJsonObject();
      Board board = createBoardFromJson(boardJson);
      JsonArray tilesArray = getTilesArray(boardJson);

      createTiles(board, tilesArray);
      connectTilesAndAddActions(board, tilesArray);

      return board;
    } catch (IOException e) {
      throw new BoardParsingException("Failed to read board file: " + e.getMessage(), e);
    } catch (JsonSyntaxException e) {
      throw new BoardParsingException("Invalid JSON syntax: " + e.getMessage(), e);
    } catch (JsonParseException e) {
      throw new BoardParsingException("Failed to parse JSON: " + e.getMessage(), e);
    } catch (BoardParsingException e) {
      throw e;
    } catch (Exception e) {
      throw new BoardParsingException("Unexpected error: " + e.getMessage(), e);
    }
  }

  @Override
  public Board readBoardFromString(String boardData) throws BoardParsingException {
    try {
      return gson.fromJson(boardData, Board.class);
    } catch (Exception e) {
      throw new BoardParsingException("Failed to parse board data: " + e.getMessage(), e);
    }
  }

  private void validatePath(Path path) {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null.");
    }
  }

  private Board createBoardFromJson(JsonObject boardJson) throws BoardParsingException {
    validateBoardProperties(boardJson);

    String name = boardJson.get("name").getAsString();
    String description = boardJson.get("description").getAsString();
    int rows = boardJson.get("rows").getAsInt();
    int cols = boardJson.get("columns").getAsInt();
    int startTileId = boardJson.get("startTileId").getAsInt();
    int endTileId = boardJson.get("endTileId").getAsInt();

    return new Board(name, description, rows, cols, startTileId, endTileId);
  }

  private void validateBoardProperties(JsonObject boardJson) throws BoardParsingException {
    if (!boardJson.has("rows")
        || !boardJson.has("columns")
        || !boardJson.has("name")
        || !boardJson.has("description")
        || !boardJson.has("startTileId")
        || !boardJson.has("endTileId")) {
      throw new BoardParsingException("Board must have name, description, rows, and columns");
    }
  }

  private JsonArray getTilesArray(JsonObject boardJson) throws BoardParsingException {
    if (!boardJson.has("tiles")) {
      throw new BoardParsingException("Board must have a 'tiles' array");
    }

    JsonArray tilesArray = boardJson.getAsJsonArray("tiles");

    if (tilesArray == null) {
      throw new BoardParsingException("Board 'tiles' field must be a valid array");
    }

    return tilesArray;
  }

  private void createTiles(Board board, JsonArray tilesArray) throws BoardParsingException {
    for (JsonElement tileElement : tilesArray) {
      Tile tile = getTile(tileElement);
      board.addTile(tile);
    }
  }

  private void connectTilesAndAddActions(Board board, JsonArray tilesArray)
      throws BoardParsingException {
    for (JsonElement tileElement : tilesArray) {
      JsonObject tileObject = tileElement.getAsJsonObject();
      int id = tileObject.get("id").getAsInt();
      Tile tile = board.getTile(id);

      connectToNextTile(board, tile, tileObject);
      addTileAction(board, tile, tileObject);
    }
  }

  private void connectToNextTile(Board board, Tile tile, JsonObject tileObject) {
    if (tileObject.has("nextTileId")) {
      int nextTileId = tileObject.get("nextTileId").getAsInt();
      Tile nextTile = board.getTile(nextTileId);
      if (nextTile != null) {
        tile.setNextTile(nextTile);
      }
    }
  }

  private void addTileAction(Board board, Tile tile, JsonObject tileObject)
      throws BoardParsingException {
    if (tileObject.has("action")) {
      JsonObject actionObject = tileObject.getAsJsonObject("action");
      validateActionType(actionObject);
      String actionType = actionObject.get("type").getAsString();
      TileAction action = createAction(actionType, actionObject, board);
      tile.setLandAction(action);
    }
  }

  private void validateActionType(JsonObject actionObject) throws BoardParsingException {
    if (!actionObject.has(TYPE_PROPERTY)) {
      throw new BoardParsingException("Action must have type");
    }
  }

  private TileAction createAction(String actionType, JsonObject actionObject, Board board)
      throws BoardParsingException {
    return switch (actionType) {
      case LADDER_ACTION -> createLadderAction(actionObject, board);
      case SNAKE_ACTION -> createSnakeAction(actionObject, board);
      case "SkipTurnAction" -> new SkipTurnAction();
      case "PropertyAction" -> createPropertyAction(actionObject);
      case "TaxAction" -> createTaxAction(actionObject);
      case "StartAction" -> createStartAction(actionObject);
      default -> throw new BoardParsingException("Unknown action type: " + actionType);
    };
  }

  private TileAction createLadderAction(JsonObject actionObject, Board board)
      throws BoardParsingException {
    validateDestinationTileId(actionObject, LADDER_ACTION);
    int destinationTileId = actionObject.get(DESTINATION_TILE_ID_PROPERTY).getAsInt();
    Tile destinationTile = getDestinationTile(board, destinationTileId, LADDER_ACTION);
    return new LadderAction(destinationTile);
  }

  private TileAction createSnakeAction(JsonObject actionObject, Board board)
      throws BoardParsingException {
    validateDestinationTileId(actionObject, SNAKE_ACTION);
    int destinationTileId = actionObject.get(DESTINATION_TILE_ID_PROPERTY).getAsInt();
    Tile destinationTile = getDestinationTile(board, destinationTileId, SNAKE_ACTION);
    return new SnakeAction(destinationTile);
  }

  private void validateDestinationTileId(JsonObject actionObject, String actionName)
      throws BoardParsingException {
    if (!actionObject.has(DESTINATION_TILE_ID_PROPERTY)) {
      throw new BoardParsingException(actionName + " must have destinationTileId");
    }
  }

  private Tile getDestinationTile(Board board, int destinationTileId, String actionName)
      throws BoardParsingException {
    Tile destinationTile = board.getTile(destinationTileId);
    if (destinationTile == null) {
      throw new BoardParsingException("Destination tile not found for " + actionName);
    }
    return destinationTile;
  }

  private TileAction createPropertyAction(JsonObject actionObject) throws BoardParsingException {
    if (!actionObject.has(PRICE_PROPERTY)) {
      throw new BoardParsingException("PropertyAction must have price");
    }
    int price = actionObject.get(PRICE_PROPERTY).getAsInt();
    return new PropertyAction(price);
  }

  private TileAction createTaxAction(JsonObject actionObject) throws BoardParsingException {
    if (!actionObject.has(AMOUNT_PROPERTY)) {
      throw new BoardParsingException("TaxAction must have amount");
    }
    int amount = actionObject.get(AMOUNT_PROPERTY).getAsInt();
    return new TaxAction(amount);
  }

  private TileAction createStartAction(JsonObject actionObject) throws BoardParsingException {
    if (!actionObject.has(AMOUNT_PROPERTY)) {
      throw new BoardParsingException("StartAction must have amount");
    }
    int amount = actionObject.get(AMOUNT_PROPERTY).getAsInt();
    return new StartAction(amount);
  }

  private Tile getTile(JsonElement tileElement) throws BoardParsingException {
    JsonObject tileObject = tileElement.getAsJsonObject();

    if (!tileObject.has("id")) {
      throw new BoardParsingException("Tile must have id, row, and column");
    }

    int id = tileObject.get("id").getAsInt();
    Tile tile = new Tile(id);

    if (tileObject.has("row")) {
      int row = tileObject.get("row").getAsInt();
      tile.setRow(row);
    }

    if (tileObject.has("column")) {
      int column = tileObject.get("column").getAsInt();
      tile.setColumn(column);
    }

    if (tileObject.has("name")) {
      String name = tileObject.get("name").getAsString();
      tile.setName(name);
    }

    return tile;
  }
}

package edu.ntnu.stud.boardgame.io.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementation of {@link BoardFileWriter} that uses Google's Gson library for JSON serialization.
 *
 * <p>This class provides functionality to serialize {@link Board} objects into JSON format and save
 * them to the filesystem. It handles the conversion of complex board structures, including tiles
 * and their associated actions, into a structured JSON representation.
 *
 * <p>The writer handles different types of tile actions by converting them to appropriate JSON
 * objects with type information and relevant properties.
 *
 * @see BoardFileWriter
 * @see Board
 * @see TileAction
 */
public class BoardFileWriterGson implements BoardFileWriter {

  private final Gson gson;

  /** Constructs a new BoardFileWriterGson with a default Gson instance. */
  public BoardFileWriterGson() {
    this.gson = new GsonBuilder().setPrettyPrinting().create();
  }

  private static JsonObject getActionObject(Tile tile, TileAction action)
      throws BoardWritingException {
    JsonObject actionObject = new JsonObject();

    if (action instanceof LadderAction ladderAction) {
      actionObject.addProperty("type", "LadderAction");
      actionObject.addProperty("destinationTileId", ladderAction.getDestinationTile().getTileId());
      actionObject.addProperty(
          "description",
          "Ladder from "
              + tile.getTileId()
              + " to "
              + ladderAction.getDestinationTile().getTileId());
    } else if (action instanceof SnakeAction snakeAction) {
      actionObject.addProperty("type", "SnakeAction");
      actionObject.addProperty("destinationTileId", snakeAction.getDestinationTile().getTileId());
      actionObject.addProperty(
          "description",
          "Snake from " + tile.getTileId() + " to " + snakeAction.getDestinationTile().getTileId());
    } else if (action instanceof SkipTurnAction) {
      actionObject.addProperty("type", "SkipTurnAction");
      actionObject.addProperty("description", "Skip turn for player");
    } else if (action instanceof PropertyAction propertyAction) {
      actionObject.addProperty("type", "PropertyAction");
      actionObject.addProperty("price", propertyAction.getPrice());
      actionObject.addProperty("description", "Property with price: " + propertyAction.getPrice());
    } else if (action instanceof TaxAction taxAction) {
      actionObject.addProperty("type", "TaxAction");
      actionObject.addProperty("amount", taxAction.getAmount());
      actionObject.addProperty("description", "Tax with amount: " + taxAction.getAmount());
    } else if (action instanceof StartAction startAction) {
      actionObject.addProperty("type", "StartAction");
      actionObject.addProperty("amount", startAction.getAmount());
      actionObject.addProperty("description", "Start tile with amount: " + startAction.getAmount());
    } else {
      throw new BoardWritingException("Unknown action type: " + action.getClass());
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
   * Writes a board object to the specified file path in JSON format.
   *
   * <p>This implementation serializes the board structure including its metadata (name,
   * description, dimensions), tiles, and actions into a JSON file.
   *
   * <p>The JSON structure includes:
   *
   * <ul>
   *   <li>Board metadata (name, description, rows, columns)
   *   <li>Start and end tile references
   *   <li>An array of all tiles with their properties
   *   <li>Actions associated with each tile
   * </ul>
   *
   * @param path the path where the board should be written
   * @param board the board object to serialize and save
   * @throws BoardWritingException if any errors occur during the writing process
   * @throws IllegalArgumentException if path or board is null
   */
  @Override
  public void writeBoard(Path path, Board board) throws BoardWritingException {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null.");
    }
    if (board == null) {
      throw new IllegalArgumentException("Board cannot be null.");
    }

    try {
      String jsonContent = writeBoardToString(board);
      Files.writeString(path, jsonContent);
    } catch (IOException e) {
      throw new BoardWritingException("Failed to write board file: " + e.getMessage(), e);
    }
  }

  @Override
  public String writeBoardToString(Board board) throws BoardWritingException {
    try {
      return gson.toJson(board);
    } catch (Exception e) {
      throw new BoardWritingException("Failed to serialize board: " + e.getMessage(), e);
    }
  }
}

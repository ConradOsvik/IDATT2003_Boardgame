package edu.ntnu.stud.boardgame.io.board;

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
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import edu.ntnu.stud.boardgame.model.action.TileAction;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class BoardFileReaderGson implements BoardFileReader {

  private static Tile getTile(JsonElement tileElement) throws BoardParsingException {
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

  @Override
  public Board readBoard(Path path) throws BoardParsingException {
    try (Reader reader = Files.newBufferedReader(path)) {
      JsonObject boardJson = JsonParser.parseReader(reader).getAsJsonObject();

      if (!boardJson.has("rows") || !boardJson.has("columns") || !boardJson.has("name")
          || !boardJson.has("description") || !boardJson.has("startTileId") || !boardJson.has(
          "endTileId")) {
        throw new BoardParsingException("Board must have name, description, rows, and columns");
      }

      String name = boardJson.get("name").getAsString();
      String description = boardJson.get("description").getAsString();
      int rows = boardJson.get("rows").getAsInt();
      int cols = boardJson.get("columns").getAsInt();
      int startTileId = boardJson.get("startTileId").getAsInt();
      int endTileId = boardJson.get("endTileId").getAsInt();

      Board board = new Board(name, description, rows, cols, startTileId, endTileId);

      JsonArray tilesArray = boardJson.getAsJsonArray("tiles");

      for (JsonElement tileElement : tilesArray) {
        Tile tile = getTile(tileElement);
        board.addTile(tile);
      }

      for (JsonElement tileElement : tilesArray) {
        JsonObject tileObject = tileElement.getAsJsonObject();

        int id = tileObject.get("id").getAsInt();
        Tile tile = board.getTile(id);

        if (tileObject.has("nextTileId")) {
          int nextTileId = tileObject.get("nextTileId").getAsInt();
          Tile nextTile = board.getTile(nextTileId);
          if (nextTile != null) {
            tile.setNextTile(nextTile);
          }
        }

        if (tileObject.has("action")) {
          JsonObject actionObject = tileObject.getAsJsonObject("action");

          if (!actionObject.has("type")) {
            throw new BoardParsingException("Action must have type");
          }

          String actionType = actionObject.get("type").getAsString();

          switch (actionType) {
            case "LadderAction" -> {
              if (!actionObject.has("destinationTileId")) {
                throw new BoardParsingException("LadderAction must have destinationTileId");
              }

              int destinationTileId = actionObject.get("destinationTileId").getAsInt();
              Tile destinationTile = board.getTile(destinationTileId);

              if (destinationTile == null) {
                throw new BoardParsingException("Destination tile not found for LadderAction");
              }

              TileAction action = new LadderAction(destinationTile);
              tile.setLandAction(action);
            }
            case "SnakeAction" -> {
              if (!actionObject.has("destinationTileId")) {
                throw new BoardParsingException("SnakeAction must have destinationTileId");
              }

              int destinationTileId = actionObject.get("destinationTileId").getAsInt();
              Tile destinationTile = board.getTile(destinationTileId);

              if (destinationTile == null) {
                throw new BoardParsingException("Destination tile not found for SnakeAction");
              }

              TileAction action = new SnakeAction(destinationTile);
              tile.setLandAction(action);
            }
            case "SkipTurnAction" -> {
              TileAction action = new SkipTurnAction();
              tile.setLandAction(action);
            }
          }
        }
      }

      return board;

    } catch (IOException e) {
      throw new BoardParsingException("Failed to read board file: " + e.getMessage(), e);
    } catch (JsonSyntaxException e) {
      throw new BoardParsingException("Invalid JSON syntax: " + e.getMessage(), e);
    } catch (JsonParseException e) {
      throw new BoardParsingException("Failed to parse JSON: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new BoardParsingException("Unexpected error: " + e.getMessage(), e);
    }
  }
}
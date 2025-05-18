package edu.ntnu.stud.boardgame.io.board;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.exception.files.BoardWritingException;
import edu.ntnu.stud.boardgame.model.Board;
import edu.ntnu.stud.boardgame.model.Tile;
import edu.ntnu.stud.boardgame.model.action.LadderAction;
import edu.ntnu.stud.boardgame.model.action.SkipTurnAction;
import edu.ntnu.stud.boardgame.model.action.SnakeAction;
import edu.ntnu.stud.boardgame.model.action.TileAction;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public class BoardFileWriterGson implements BoardFileWriter {

  private final Gson gson;

  public BoardFileWriterGson() {
    this.gson = new Gson();
  }

  private static JsonObject getActionObject(Tile tile, TileAction action)
      throws BoardWritingException {
    JsonObject actionObject = new JsonObject();

    if (action instanceof LadderAction ladderAction) {
      actionObject.addProperty("type", "LadderAction");
      actionObject.addProperty("destinationTileId", ladderAction.getDestinationTile().getTileId());
      actionObject.addProperty("description",
          "Ladder from " + tile.getTileId() + " to " + ladderAction.getDestinationTile()
              .getTileId());
    } else if (action instanceof SnakeAction snakeAction) {
      actionObject.addProperty("type", "SnakeAction");
      actionObject.addProperty("destinationTileId", snakeAction.getDestinationTile().getTileId());
      actionObject.addProperty("description",
          "Snake from " + tile.getTileId() + " to " + snakeAction.getDestinationTile().getTileId());
    } else if (action instanceof SkipTurnAction skipTurnAction) {
      actionObject.addProperty("type", "SkipTurnAction");
      actionObject.addProperty("description",
          "Skip turn for player");
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

  @Override
  public void writeBoard(Path path, Board board) throws BoardWritingException {
    try {
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

      try (FileWriter writer = new FileWriter(path.toFile())) {
        gson.toJson(boardJson, writer);
      }
    } catch (IOException e) {
      throw new BoardWritingException("Failed to write board file: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new BoardWritingException("Unexpected error: " + e.getMessage(), e);
    }
  }
}

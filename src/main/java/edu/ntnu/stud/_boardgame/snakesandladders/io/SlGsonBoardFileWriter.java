package edu.ntnu.stud.boardgame.snakesandladders.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.io.board.GsonBoardFileWriter;
import edu.ntnu.stud.boardgame.core.model.Tile;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import edu.ntnu.stud.boardgame.snakesandladders.model.action.LadderAction;
import edu.ntnu.stud.boardgame.snakesandladders.model.action.SnakeAction;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SlGsonBoardFileWriter extends GsonBoardFileWriter<SlBoard> {

  public void write(Path path, SlBoard board) throws IOException {
    JsonObject boardJson = new JsonObject();

    boardJson.addProperty("name", board.getName());
    boardJson.addProperty("description", board.getDescription());

    JsonArray tilesArray = new JsonArray();
    List<Tile> tiles = board.getTiles();

    for (Tile tile : tiles) {
      JsonObject tileObject = new JsonObject();

      tileObject.addProperty("id", tile.getTileId());
      tileObject.addProperty("nextTile", tile.getNextTile().getTileId());

      if (tile.getLandAction() != null) {
        JsonObject actionObject = new JsonObject();
        Tile destinationTile = null;
        String actionType = null;

        if (tile.getLandAction() instanceof LadderAction ladderAction) {
          actionType = "ladderAction";
          destinationTile = ladderAction.getDestinationTile();
        } else if (tile.getLandAction() instanceof SnakeAction snakeAction) {
          actionType = "snakeAction";
          destinationTile = snakeAction.getDestinationTile();
        }

        if (actionType != null && destinationTile != null) {
          actionObject.addProperty("type", actionType);
          actionObject.addProperty("destinationTileId", destinationTile.getTileId());
          actionObject.addProperty("description", actionType + " from " +
              tile.getTileId() + " to " + destinationTile.getTileId());
          tileObject.add("action", actionObject);
        }
      }

      tilesArray.add(tileObject);
    }
    boardJson.add("tiles", tilesArray);

    try (FileWriter writer = new FileWriter(path.toFile())) {
      gson.toJson(boardJson, writer);
    }
  }
}

package edu.ntnu.stud.boardgame.snakesandladders.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.io.board.GsonBoardFileReader;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlBoard;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class SlGsonBoardFileReader extends GsonBoardFileReader<SlBoard> {

  public SlBoard read(Path path) throws IOException {
    try (Reader reader = Files.newBufferedReader(path)) {
      JsonObject boardJson = gson.fromJson(reader, JsonObject.class);

      String name = boardJson.get("name").getAsString();
      String description = boardJson.get("description").getAsString();

      int rows = 10;
      int columns = 9;
      if (boardJson.has("rows") && boardJson.has("columns")) {
        rows = boardJson.get("rows").getAsInt();
        columns = boardJson.get("columns").getAsInt();
      }

      SlBoard board = new SlBoard(name, description, rows, columns);
      board.initializeBoard();

      JsonArray tilesArray = boardJson.getAsJsonArray("tiles");
      for (JsonElement tileElement : tilesArray) {
        JsonObject tileObject = tileElement.getAsJsonObject();

        if (tileObject.has("action")) {
          JsonObject actionObject = tileObject.getAsJsonObject("action");
          String actionType = actionObject.get("type").getAsString();
          int tileId = tileObject.get("id").getAsInt();
          int destinationTileId = actionObject.get("destinationTileId").getAsInt();

          if (actionType.equals("ladderAction")) {
            board.addLadder(tileId, destinationTileId);
          } else if (actionType.equals("snakeAction")) {
            board.addSnake(tileId, destinationTileId);
          }
        }
      }

      return board;
    } catch (Exception e) {
      throw new IOException("Failed to read board file: " + e.getMessage(), e);
    }
  }
}

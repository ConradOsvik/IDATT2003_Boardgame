package edu.ntnu.stud.boardgame.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Tile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for saving and loading board configurations to/from JSON files.
 */
public class BoardFileController {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static class Connection {
        @SerializedName("direction")
        String direction;
        @SerializedName("targetTileId")
        int targetTileId;
    }

    private static class TileData {
        @SerializedName("id")
        int id;
        @SerializedName("connections")
        List<Connection> connections;
    }

    private static class BoardData {
        @SerializedName("tiles")
        List<TileData> tiles;
    }

    /**
     * Saves the current board configuration from a board game to a JSON file.
     *
     * @param boardGame The board game containing the board to save
     * @param filePath The path where to save the JSON file
     * @throws IOException If an I/O error occurs during the save operation
     */
    public void saveBoard(BoardGame boardGame, String filePath) throws IOException {
        BoardData boardData = new BoardData();
        boardData.tiles = new ArrayList<>();

        // For each tile, we need to save its ID and connections
        for (int i = 1; i <= 90; i++) {
            Tile tile = boardGame.getBoard().getTile(i);
            if (tile != null) {
                TileData tileData = new TileData();
                tileData.id = tile.getTileId();
                tileData.connections = new ArrayList<>();

                for (Tile.Direction direction : Tile.Direction.values()) {
                    List<Tile> connectedTiles = tile.getConnectedTiles(direction);
                    for (Tile connectedTile : connectedTiles) {
                        Connection connection = new Connection();
                        connection.direction = direction.name();
                        connection.targetTileId = connectedTile.getTileId();
                        tileData.connections.add(connection);
                    }
                }

                boardData.tiles.add(tileData);
            }
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(boardData, writer);
        }
    }

    /**
     * Loads a board configuration from a JSON file and sets it up in the board game.
     *
     * @param boardGame The board game to load the board configuration into
     * @param filePath The path of the JSON file to load
     * @throws IOException If an I/O error occurs during the load operation
     */
    public void loadBoard(BoardGame boardGame, String filePath) throws IOException {
        BoardData boardData;
        try (FileReader reader = new FileReader(filePath)) {
            boardData = gson.fromJson(reader, BoardData.class);
        }

        // First create a new board
        boardGame.createEmptyBoard();

        // Create tiles
        for (TileData tileData : boardData.tiles) {
            boardGame.getBoard().addTile(new Tile(tileData.id));
        }

        // Set up connections
        for (TileData tileData : boardData.tiles) {
            Tile tile = boardGame.getBoard().getTile(tileData.id);
            
            if (tileData.connections != null) {
                for (Connection connection : tileData.connections) {
                    Tile.Direction direction = Tile.Direction.valueOf(connection.direction);
                    Tile targetTile = boardGame.getBoard().getTile(connection.targetTileId);
                    
                    if (targetTile != null) {
                        tile.addConnectedTile(direction, targetTile);
                    }
                }
            }
        }
    }
} 
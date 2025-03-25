package edu.ntnu.stud.boardgame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.ntnu.stud.boardgame.core.model.Board;
import edu.ntnu.stud.boardgame.core.model.BoardGame;
import edu.ntnu.stud.boardgame.core.model.Tile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for saving and loading board configurations to/from JSON files.
 */
public class BoardFileController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the current board configuration from a board game to a JSON file.
     *
     * @param boardGame The board game containing the board to save
     * @param filePath The path where to save the JSON file
     * @throws IOException If an I/O error occurs during the save operation
     */
    public void saveBoard(BoardGame boardGame, String filePath) throws IOException {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ArrayNode tilesArray = rootNode.putArray("tiles");
        
        // For each tile, we need to save its ID and connections
        for (int i = 1; i <= 90; i++) {
            Tile tile = boardGame.getBoard().getTile(i);
            if (tile != null) {
                ObjectNode tileNode = objectMapper.createObjectNode();
                tileNode.put("id", tile.getTileId());
                
                ArrayNode connectionsArray = tileNode.putArray("connections");
                for (Tile.Direction direction : Tile.Direction.values()) {
                    List<Tile> connectedTiles = tile.getConnectedTiles(direction);
                    for (Tile connectedTile : connectedTiles) {
                        ObjectNode connectionNode = objectMapper.createObjectNode();
                        connectionNode.put("direction", direction.name());
                        connectionNode.put("targetTileId", connectedTile.getTileId());
                        connectionsArray.add(connectionNode);
                    }
                }
                
                tilesArray.add(tileNode);
            }
        }
        
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), rootNode);
    }

    /**
     * Loads a board configuration from a JSON file and sets it up in the board game.
     *
     * @param boardGame The board game to load the board configuration into
     * @param filePath The path of the JSON file to load
     * @throws IOException If an I/O error occurs during the load operation
     */
    public void loadBoard(BoardGame boardGame, String filePath) throws IOException {
        String jsonContent = Files.readString(Paths.get(filePath));
        ObjectNode rootNode = (ObjectNode) objectMapper.readTree(jsonContent);
        
        // First create a new board
        boardGame.createEmptyBoard();
        
        // Create tiles
        ArrayNode tilesArray = (ArrayNode) rootNode.get("tiles");
        for (int i = 0; i < tilesArray.size(); i++) {
            ObjectNode tileNode = (ObjectNode) tilesArray.get(i);
            int tileId = tileNode.get("id").asInt();
            boardGame.getBoard().addTile(new Tile(tileId));
        }
        
        // Set up connections
        for (int i = 0; i < tilesArray.size(); i++) {
            ObjectNode tileNode = (ObjectNode) tilesArray.get(i);
            int tileId = tileNode.get("id").asInt();
            Tile tile = boardGame.getBoard().getTile(tileId);
            
            ArrayNode connectionsArray = (ArrayNode) tileNode.get("connections");
            if (connectionsArray != null) {
                for (int j = 0; j < connectionsArray.size(); j++) {
                    ObjectNode connectionNode = (ObjectNode) connectionsArray.get(j);
                    String directionStr = connectionNode.get("direction").asText();
                    int targetTileId = connectionNode.get("targetTileId").asInt();
                    
                    Tile.Direction direction = Tile.Direction.valueOf(directionStr);
                    Tile targetTile = boardGame.getBoard().getTile(targetTileId);
                    
                    if (targetTile != null) {
                        tile.addConnectedTile(direction, targetTile);
                    }
                }
            }
        }
    }
} 
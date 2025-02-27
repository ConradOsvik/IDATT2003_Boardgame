package edu.ntnu.stud.boardgame.model;

import java.util.HashMap;
import java.util.Map;

class Board {
    private Map<Integer, Tile> tiles = new HashMap<>();

    public void addTile(Tile tile) {
        tiles.put(tile.getTileId(), tile);
    }

    public Tile getTile(int tileId) {
        return tiles.get(tileId);
    }
}
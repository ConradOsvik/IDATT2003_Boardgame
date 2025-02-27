package edu.ntnu.stud.boardgame.model;

import edu.ntnu.stud.boardgame.model.actions.TileAction;

class Tile {
    private int tileId;
    private Tile nextTile;
    private TileAction landAction;

    public Tile(int tileId) {
        this.tileId = tileId;
    }

    public int getTileId() {
        return tileId;
    }

    public void landPlayer(Player player) {
        if(player == null) {
            throw new NullPointerException("Player cannot be null");
        }

        if (landAction != null) {
            landAction.perform(player);
        }
    }

    public void leavePlayer(Player player) {
        //TODO: Implement
    }

    public void setNextTile(Tile nextTile) {
        this.nextTile = nextTile;
    }

    public Tile getNextTile() {
        return nextTile;
    }

    public void setLandAction(TileAction landAction) {
        this.landAction = landAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return tileId == tile.tileId;
    }
}
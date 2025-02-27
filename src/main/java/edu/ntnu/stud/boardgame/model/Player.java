package edu.ntnu.stud.boardgame.model;

public class Player {
    private String name;
    private Tile currentTile;

    public Player(String name, BoardGame game) {
        this.name = name;
    }

    public void placeOnTile(Tile tile) {
        if (currentTile != null) {
            currentTile.leavePlayer(this);
        }
        currentTile = tile;
        tile.landPlayer(this);
    }

    public void move(int steps) {
        Tile targetTile = currentTile;
        for (int i = 0; i < steps && targetTile != null; i++) {
            targetTile = targetTile.getNextTile();

        }
        if (targetTile != null) {
            placeOnTile(targetTile);
        }
    }

    public Tile getCurrentTile() {
        return currentTile;
    }
}
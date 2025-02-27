package edu.ntnu.stud.boardgame.model.actions;

import edu.ntnu.stud.boardgame.model.Player;

public interface TileAction {
    void perform(Player player);
}
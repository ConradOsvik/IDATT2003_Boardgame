package edu.ntnu.stud.boardgame.core.factory;

import edu.ntnu.stud.boardgame.core.model.Player;

public interface PlayerFactory<P extends Player> {

  P createPlayer(String name, int tokenId);
}

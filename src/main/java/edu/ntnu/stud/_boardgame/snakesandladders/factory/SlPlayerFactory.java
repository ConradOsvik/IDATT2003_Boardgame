package edu.ntnu.stud.boardgame.snakesandladders.factory;

import edu.ntnu.stud.boardgame.core.factory.PlayerFactory;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;

public class SlPlayerFactory implements PlayerFactory<SlPlayer> {

  @Override
  public SlPlayer createPlayer(String name, int tokenId) {
    return new SlPlayer(name, tokenId);
  }
}
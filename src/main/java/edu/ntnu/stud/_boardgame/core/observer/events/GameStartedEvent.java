package edu.ntnu.stud.boardgame.core.observer.events;

import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.core.observer.GameEvent;
import java.util.List;

public class GameStartedEvent extends GameEvent {

  protected final List<Player> players;

  public GameStartedEvent(List<Player> players) {
    super(EventType.GAME_STARTED);
    this.players = players;
  }

  public List<Player> getPlayers() {
    return players;
  }
}

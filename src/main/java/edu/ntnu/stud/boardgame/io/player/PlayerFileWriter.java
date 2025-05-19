package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerWritingException;
import edu.ntnu.stud.boardgame.model.Player;
import java.nio.file.Path;
import java.util.List;

public interface PlayerFileWriter {

  void writePlayers(Path path, List<Player> players) throws PlayerWritingException;
}

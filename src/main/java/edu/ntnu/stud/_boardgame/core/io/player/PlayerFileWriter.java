package edu.ntnu.stud.boardgame.core.io.player;

import edu.ntnu.stud.boardgame.core.model.Player;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface PlayerFileWriter<P extends Player> {

  void write(Path path, List<P> player) throws IOException;
}

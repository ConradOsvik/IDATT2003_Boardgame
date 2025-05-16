package edu.ntnu.stud.boardgame.core.io.player;

import edu.ntnu.stud.boardgame.core.model.Player;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface PlayerFileReader<P extends Player> {

  List<P> read(Path path) throws IOException;
}

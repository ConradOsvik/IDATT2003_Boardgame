package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerParsingException;
import edu.ntnu.stud.boardgame.model.Player;
import java.nio.file.Path;
import java.util.List;

public interface PlayerFileReader {

  List<Player> readPlayers(Path path) throws PlayerParsingException;
}

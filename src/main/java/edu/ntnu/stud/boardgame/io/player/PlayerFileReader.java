package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerParsingException;
import edu.ntnu.stud.boardgame.model.Player;
import java.nio.file.Path;
import java.util.List;

/**
 * An interface for reading {@link Player} objects from persistent storage.
 *
 * <p>Implementations of this interface are responsible for deserializing player data from a
 * specified file format. This interface is part of the I/O framework for player management in the
 * board game application.</p>
 *
 * @see edu.ntnu.stud.boardgame.io.player.PlayerFileWriter
 */
public interface PlayerFileReader {

  /**
   * Reads player objects from the specified file path.
   *
   * <p>This method deserializes player data from the file at the given path and returns a list
   * of {@link Player} objects. The file format is determined by the specific implementation.</p>
   *
   * @param path the path to the file containing player data
   * @return a list of player objects parsed from the file
   * @throws PlayerParsingException if any errors occur during the reading or parsing process, such
   *                                as I/O errors, invalid file format, or corrupted data
   */
  List<Player> readPlayers(Path path) throws PlayerParsingException;
}

package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerWritingException;
import edu.ntnu.stud.boardgame.model.Player;
import java.nio.file.Path;
import java.util.List;

/**
 * An interface for writing {@link Player} objects to persistent storage.
 *
 * <p>Implementations of this interface are responsible for serializing player data to a specified
 * file format. This interface is part of the I/O framework for player management in the board game
 * application.
 *
 * <p>Client code can use implementations of this interface to save player data to files without
 * being concerned with the specific file format or serialization details.
 *
 * @see edu.ntnu.stud.boardgame.io.player.PlayerFileReader
 * @see edu.ntnu.stud.boardgame.model.Player
 */
public interface PlayerFileWriter {

  /**
   * Writes a list of player objects to the specified file path.
   *
   * <p>This method serializes player data to the file at the given path. The file format is
   * determined by the specific implementation.
   *
   * @param path the path to the file where player data should be written
   * @param players the list of player objects to be serialized and saved
   * @throws PlayerWritingException if any errors occur during the writing process, such as I/O
   *     errors or serialization failures
   */
  void writePlayers(Path path, List<Player> players) throws PlayerWritingException;
}

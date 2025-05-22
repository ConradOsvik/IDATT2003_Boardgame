package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerWritingException;
import edu.ntnu.stud.boardgame.model.Player;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Implementation of {@link PlayerFileWriter} that writes player data to CSV files.
 *
 * <p>This class provides functionality to serialize {@link Player} objects into CSV format.
 * Each player is written as a single line in the CSV file with the format:
 * {@code player_name,piece_type}</p>
 *
 * <p>The implementation writes player name and the display name of the player's piece type,
 * separated by a comma. Each player is written on a separate line in the output file.</p>
 *
 * @see PlayerFileWriter
 * @see Player
 * @see edu.ntnu.stud.boardgame.model.enums.PieceType
 */
public class PlayerFileWriterCsv implements PlayerFileWriter {

  /**
   * Writes a list of player objects to a CSV file at the specified path.
   *
   * <p>The method serializes each player in the list to a line in the CSV file
   * with the format: {@code player_name,piece_type}. The piece type is represented by its display
   * name.</p>
   *
   * <p>The implementation validates the input parameters and handles the following cases:
   * <ul>
   *   <li>If the path is null, a PlayerWritingException is thrown</li>
   *   <li>If the players list is null or empty, a PlayerWritingException is thrown</li>
   *   <li>Any I/O errors are wrapped in a PlayerWritingException</li>
   * </ul>
   * </p>
   *
   * @param path    the path to the file where player data should be written
   * @param players the list of player objects to be serialized and saved
   * @throws PlayerWritingException if any errors occur during the writing process, such as null
   *                                path, empty player list, I/O errors, or other unexpected
   *                                exceptions
   */
  @Override
  public void writePlayers(Path path, List<Player> players) throws PlayerWritingException {
    if (path == null) {
      throw new PlayerWritingException("Path cannot be null.");
    }
    if (players == null || players.isEmpty()) {

      throw new PlayerWritingException("Player list is null or empty");
    }

    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      for (Player player : players) {
        writer.write(player.getName() + "," + player.getPiece().getDisplayName());
        writer.newLine();
      }
    } catch (IOException e) {
      throw new PlayerWritingException("Failed to write players to file: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new PlayerWritingException("Unexpected error: " + e.getMessage(), e);
    }
  }
}

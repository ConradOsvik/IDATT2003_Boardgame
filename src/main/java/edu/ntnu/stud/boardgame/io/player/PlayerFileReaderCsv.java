package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerParsingException;
import edu.ntnu.stud.boardgame.model.Player;
import edu.ntnu.stud.boardgame.model.enums.PieceType;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link PlayerFileReader} that reads player data from CSV files.
 *
 * <p>This class provides functionality to deserialize {@link Player} objects from CSV format. Each
 * line in the CSV file represents a player with comma-separated values for name and piece type. The
 * expected format is: {@code player_name,piece_type}
 *
 * <p>Empty lines in the CSV file are ignored. The implementation validates each line of player data
 * and converts piece type strings to {@link PieceType} enum values.
 *
 * @see PlayerFileReader
 * @see Player
 * @see PieceType
 */
public class PlayerFileReaderCsv implements PlayerFileReader {

  /**
   * Reads player objects from a CSV file at the specified path.
   *
   * <p>The method parses each line of the CSV file with the expected format: {@code
   * player_name,piece_type}. It creates a {@link Player} object for each valid line, using the name
   * and converting the piece type string to a {@link PieceType} enum value.
   *
   * <p>The implementation handles the following cases:
   *
   * <ul>
   *   <li>Empty lines are skipped
   *   <li>Lines with incorrect format throw a {@link PlayerParsingException}
   *   <li>Invalid piece types throw a {@link PlayerParsingException}
   * </ul>
   *
   * @param path the path to the CSV file containing player data
   * @return a list of player objects parsed from the file
   * @throws PlayerParsingException if any errors occur during reading or parsing the file, such as
   *     I/O errors, invalid format, or invalid piece types
   */
  @Override
  public List<Player> readPlayers(Path path) throws PlayerParsingException {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null.");
    }
    List<Player> players = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      String line;
      int lineNumber = 0;

      while ((line = reader.readLine()) != null) {
        lineNumber++;
        if (line.trim().isEmpty()) {
          continue;
        }

        String[] parts = line.split(",");
        if (parts.length != 2) {
          throw new PlayerParsingException("Invalid player data at line " + lineNumber);
        }

        String name = parts[0].trim();
        String pieceStr = parts[1].trim();

        if (name.isEmpty() || pieceStr.isEmpty()) {
          throw new PlayerParsingException("Invalid player data at line " + lineNumber);
        }

        try {
          PieceType piece = PieceType.fromDisplayName(pieceStr);
          players.add(new Player(name, piece));
        } catch (IllegalArgumentException e) {
          throw new PlayerParsingException("Invalid piece type at line " + lineNumber);
        }
      }

      return players;
    } catch (IOException e) {
      throw new PlayerParsingException("Failed to read players from file: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new PlayerParsingException("Unexpected error: " + e.getMessage(), e);
    }
  }
}

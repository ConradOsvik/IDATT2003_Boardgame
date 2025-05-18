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

public class PlayerFileReaderCsv implements PlayerFileReader {

  @Override
  public List<Player> readPlayers(Path path) throws PlayerParsingException {
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

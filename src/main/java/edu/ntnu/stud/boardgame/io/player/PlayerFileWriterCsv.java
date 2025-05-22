package edu.ntnu.stud.boardgame.io.player;

import edu.ntnu.stud.boardgame.exception.files.PlayerWritingException;
import edu.ntnu.stud.boardgame.model.Player;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PlayerFileWriterCsv implements PlayerFileWriter {

  @Override
  public void writePlayers(Path path, List<Player> players) throws PlayerWritingException {
    if (path == null) {
      throw new IllegalArgumentException("Path cannot be null.");
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

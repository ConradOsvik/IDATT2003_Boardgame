package edu.ntnu.stud.boardgame.core.io.player;

import edu.ntnu.stud.boardgame.core.model.Player;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvPlayerFileWriter<P extends Player> implements PlayerFileWriter<P> {

  @Override
  public void write(Path path, List<P> players) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      for (P player : players) {
        writer.write(player.getName() + "," + player.getTokenId());
        writer.newLine();
      }
    }
  }
}
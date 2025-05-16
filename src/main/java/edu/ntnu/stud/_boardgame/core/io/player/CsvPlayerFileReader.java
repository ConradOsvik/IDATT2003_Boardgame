package edu.ntnu.stud.boardgame.core.io.player;

import edu.ntnu.stud.boardgame.core.factory.PlayerFactory;
import edu.ntnu.stud.boardgame.core.model.Player;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvPlayerFileReader<P extends Player> implements PlayerFileReader<P> {

  private final PlayerFactory<P> playerFactory;

  public CsvPlayerFileReader(PlayerFactory<P> playerFactory) {
    this.playerFactory = playerFactory;
  }

  @Override
  public List<P> read(Path path) throws IOException {
    List<P> players = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(path)) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length >= 2) {
          String name = parts[0].trim();
          int tokenId;
          try {
            tokenId = Integer.parseInt(parts[1].trim());
          } catch (NumberFormatException e) {
            tokenId = Math.abs(parts[1].trim().hashCode() % 5) + 1;
          }

          P player = playerFactory.createPlayer(name, tokenId);
          players.add(player);
        }
      }
    }

    return players;
  }
}

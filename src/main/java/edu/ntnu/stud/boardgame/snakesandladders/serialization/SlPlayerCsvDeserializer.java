package edu.ntnu.stud.boardgame.snakesandladders.serialization;

import edu.ntnu.stud.boardgame.core.filehandling.PlayerCsvDeserializer;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import javafx.scene.paint.Color;

/**
 * Specialized CSV deserializer for Snakes and Ladders players.
 */
public class SlPlayerCsvDeserializer extends PlayerCsvDeserializer {

    /**
     * Deserializes a single CSV line to a Snakes and Ladders player.
     *
     * @param line the CSV line to deserialize
     * @return the deserialized SlPlayer object, or null if deserialization failed
     */
    @Override
    protected Player deserializeLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(",");
        if (parts.length >= 2) {
            String name = parts[0].trim();
            String tokenType = parts[1].trim();
            Color color = stringToColor(tokenType);
            return new SlPlayer(name, color);
        } else if (parts.length == 1 && !parts[0].trim().isEmpty()) {
            String name = parts[0].trim();
            return new SlPlayer(name, Color.RED);
        }

        return null;
    }

    /**
     * Converts a token string representation to a JavaFX Color.
     *
     * @param token the token string to convert
     * @return the corresponding JavaFX Color
     */
    private Color stringToColor(String token) {
        return switch (token) {
            case "TopHat" -> Color.RED;
            case "RaceCar" -> Color.GREEN;
            case "Cat" -> Color.BLUE;
            case "Thimble" -> Color.YELLOW;
            case "Shoe" -> Color.PURPLE;
            case "Wheelbarrow" -> Color.ORANGE;
            default -> Color.RED;
        };
    }
}
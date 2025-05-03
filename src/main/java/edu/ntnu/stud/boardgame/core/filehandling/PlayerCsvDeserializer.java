package edu.ntnu.stud.boardgame.core.filehandling;

import java.util.ArrayList;
import java.util.List;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import javafx.scene.paint.Color;

/**
 * Deserializer for converting CSV format to Player objects.
 */
public class PlayerCsvDeserializer implements Deserializer<List<Player>> {

    /**
     * Deserializes a CSV string to a list of Player objects.
     * Each line in the CSV should have a player name and token type
     * separated by a comma.
     *
     * @param serialized the CSV string to deserialize
     * @return a list of deserialized Player objects
     */
    @Override
    public List<Player> deserialize(String serialized) {
        if (serialized == null || serialized.trim().isEmpty()) {
            return new ArrayList<>();
        }

        List<Player> players = new ArrayList<>();
        String[] lines = serialized.split("\n");

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String name = parts[0].trim();
                String tokenType = parts[1].trim();
                Color color = stringToColor(tokenType);
                players.add(new SlPlayer(name, color));
            } else if (parts.length == 1 && !parts[0].trim().isEmpty()) {
                String name = parts[0].trim();
                players.add(new SlPlayer(name, Color.RED));
            }
        }

        return players;
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
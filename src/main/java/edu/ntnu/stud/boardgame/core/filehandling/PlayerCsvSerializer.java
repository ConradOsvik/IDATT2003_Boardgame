package edu.ntnu.stud.boardgame.core.filehandling;

import java.util.List;
import java.util.stream.Collectors;
import edu.ntnu.stud.boardgame.core.model.Player;

/**
 * Serializer for converting Player objects to CSV format.
 */
public class PlayerCsvSerializer implements Serializer<List<Player>> {

    /**
     * Serializes a list of Player objects to a CSV string.
     * Each player is represented as a line with name and token type
     * separated by a comma.
     *
     * @param players the list of players to serialize
     * @return a CSV-formatted string representing the players
     */
    @Override
    public String serialize(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return "";
        }

        return players.stream()
                .map(player -> {
                    StringBuilder line = new StringBuilder();
                    line.append(player.getName());

                    if (player instanceof edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer slPlayer) {
                        line.append(",").append(colorToString(slPlayer.getColor()));
                    }

                    return line.toString();
                })
                .collect(Collectors.joining("\n"));
    }

    /**
     * Converts a JavaFX Color to a string representation.
     * For the snake and ladders game, we'll use a simple naming scheme.
     *
     * @param color the JavaFX Color to convert
     * @return a string representation of the color
     */
    private String colorToString(javafx.scene.paint.Color color) {
        if (color.equals(javafx.scene.paint.Color.RED)) {
            return "TopHat";
        } else if (color.equals(javafx.scene.paint.Color.GREEN)) {
            return "RaceCar";
        } else if (color.equals(javafx.scene.paint.Color.BLUE)) {
            return "Cat";
        } else if (color.equals(javafx.scene.paint.Color.YELLOW)) {
            return "Thimble";
        } else if (color.equals(javafx.scene.paint.Color.PURPLE)) {
            return "Shoe";
        } else if (color.equals(javafx.scene.paint.Color.ORANGE)) {
            return "Wheelbarrow";
        } else {
            return "GenericToken";
        }
    }
}
package edu.ntnu.stud.boardgame.snakesandladders.serialization;

import edu.ntnu.stud.boardgame.core.filehandling.PlayerCsvSerializer;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import javafx.scene.paint.Color;

/**
 * Specialized CSV serializer for Snakes and Ladders players.
 */
public class SlPlayerCsvSerializer extends PlayerCsvSerializer {

    /**
     * Serializes a single player to a CSV line.
     *
     * @param player the player to serialize
     * @return a CSV line representing the player
     */
    @Override
    protected String serializePlayer(Player player) {
        StringBuilder line = new StringBuilder();
        line.append(player.getName());

        if (player instanceof SlPlayer slPlayer) {
            line.append(",").append(colorToString(slPlayer.getColor()));
        }

        return line.toString();
    }

    /**
     * Converts a JavaFX Color to a string representation.
     * For the snake and ladders game, we'll use a simple naming scheme.
     *
     * @param color the JavaFX Color to convert
     * @return a string representation of the color
     */
    private String colorToString(Color color) {
        if (color.equals(Color.RED)) {
            return "TopHat";
        } else if (color.equals(Color.GREEN)) {
            return "RaceCar";
        } else if (color.equals(Color.BLUE)) {
            return "Cat";
        } else if (color.equals(Color.YELLOW)) {
            return "Thimble";
        } else if (color.equals(Color.PURPLE)) {
            return "Shoe";
        } else if (color.equals(Color.ORANGE)) {
            return "Wheelbarrow";
        } else {
            return "GenericToken";
        }
    }
}
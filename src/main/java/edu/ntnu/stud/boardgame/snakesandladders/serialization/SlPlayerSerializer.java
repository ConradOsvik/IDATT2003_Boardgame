package edu.ntnu.stud.boardgame.snakesandladders.serialization;

import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.filehandling.PlayerJsonSerializer;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import javafx.scene.paint.Color;

/**
 * Specialized serializer for Snakes and Ladders players.
 */
public class SlPlayerSerializer extends PlayerJsonSerializer {

    /**
     * Populates a JsonObject with data from a Snakes and Ladders player.
     *
     * @param player     the player to serialize
     * @param jsonObject the JSON object to populate
     */
    @Override
    protected void populatePlayerJson(Player player, JsonObject jsonObject) {
        jsonObject.addProperty("type", "SlPlayer");
        jsonObject.addProperty("name", player.getName());

        if (player instanceof SlPlayer slPlayer) {
            Color color = slPlayer.getColor();
            jsonObject.addProperty("tokenType", colorToString(color));
        }
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
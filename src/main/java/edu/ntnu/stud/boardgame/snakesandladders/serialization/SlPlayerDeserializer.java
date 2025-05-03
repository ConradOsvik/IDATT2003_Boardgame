package edu.ntnu.stud.boardgame.snakesandladders.serialization;

import com.google.gson.JsonObject;
import edu.ntnu.stud.boardgame.core.filehandling.PlayerJsonDeserializer;
import edu.ntnu.stud.boardgame.core.model.Player;
import edu.ntnu.stud.boardgame.snakesandladders.model.SlPlayer;
import javafx.scene.paint.Color;

/**
 * Specialized deserializer for Snakes and Ladders players.
 */
public class SlPlayerDeserializer extends PlayerJsonDeserializer {

    /**
     * Creates a Snakes and Ladders player from a JsonObject.
     *
     * @param jsonObject the JSON object to deserialize
     * @return the deserialized SlPlayer object, or null if the type doesn't match
     */
    @Override
    protected Player createPlayerFromJson(JsonObject jsonObject) {
        if (!jsonObject.has("type") || !"SlPlayer".equals(jsonObject.get("type").getAsString())) {
            return null;
        }

        String name = jsonObject.has("name") ? jsonObject.get("name").getAsString() : "Player";
        String tokenType = jsonObject.has("tokenType") ? jsonObject.get("tokenType").getAsString() : "GenericToken";

        Color color = stringToColor(tokenType);
        return new SlPlayer(name, color);
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
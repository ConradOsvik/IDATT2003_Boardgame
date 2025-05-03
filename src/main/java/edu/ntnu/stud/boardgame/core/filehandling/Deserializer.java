package edu.ntnu.stud.boardgame.core.filehandling;

/**
 * Interface for defining deserialization operations.
 * This interface provides methods for deserializing strings to objects.
 *
 * @param <T> the type of object to deserialize to
 */
public interface Deserializer<T> {

    /**
     * Deserializes a string representation to an object.
     *
     * @param serialized the serialized string to deserialize
     * @return the deserialized object
     */
    T deserialize(String serialized);
}
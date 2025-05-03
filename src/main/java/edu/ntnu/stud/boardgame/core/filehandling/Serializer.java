package edu.ntnu.stud.boardgame.core.filehandling;

/**
 * Interface for defining serialization operations.
 * This interface provides methods for serializing objects to specific formats.
 *
 * @param <T> the type of object to serialize
 */
public interface Serializer<T> {

    /**
     * Serializes an object to a string representation.
     *
     * @param object the object to serialize
     * @return a string representation of the object
     */
    String serialize(T object);
}
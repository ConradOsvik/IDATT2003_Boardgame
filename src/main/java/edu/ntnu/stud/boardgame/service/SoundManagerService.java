package edu.ntnu.stud.boardgame.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Singleton Service for managing game sound effects, including loading, caching, and playing
 * sounds. Provides volume control and muting capabilities.
 */
public class SoundManagerService {

  private static final Logger LOGGER = Logger.getLogger(SoundManagerService.class.getName());

  private static SoundManagerService instance;

  private final Map<String, Media> soundCache;

  private double volume = 0.5;
  private boolean muted = false;

  private SoundManagerService() {
    this.soundCache = new HashMap<>();
    preloadSounds();
  }

  /**
   * Gets the singleton instance of SoundManagerService.
   *
   * @return the SoundManagerService instance
   */
  public static synchronized SoundManagerService getInstance() {
    if (instance == null) {
      instance = new SoundManagerService();
    }
    return instance;
  }

  /**
   * Preloads all game sound effects into the sound cache. This method is called during
   * initialization to ensure sounds are ready to play.
   */
  private void preloadSounds() {
    loadSound("dice_roll", "/sounds/dice_roll.wav");
    loadSound("move", "/sounds/move.wav");
    loadSound("ladder", "/sounds/ladder.wav");
    loadSound("snake", "/sounds/snake.wav");
    loadSound("freeze", "/sounds/freeze.wav");
    loadSound("bounce", "/sounds/bounce.wav");
    loadSound("victory", "/sounds/victory.mp3");
    loadSound("cash_incoming", "/sounds/cash_incoming.wav");
    loadSound("receipt", "/sounds/receipt.wav");
  }

  /**
   * Loads a sound file into the sound cache.
   *
   * @param name the identifier for the sound
   * @param path the resource path to the sound file
   */
  public void loadSound(String name, String path) {
    if (name == null || name.trim().isEmpty()) {
      LOGGER.warning("Sound name cannot be null or empty.");
      return;
    }
    if (path == null || path.trim().isEmpty()) {
      LOGGER.warning("Sound path cannot be null or empty for sound: " + name);
      return;
    }
    try {
      URL resource = getClass().getResource(path);
      if (resource != null) {
        Media media = new Media(resource.toExternalForm());
        soundCache.put(name, media);
        LOGGER.info("Loaded sound: " + name);
      } else {
        LOGGER.warning("Could not find sound resource: " + path);
      }
    } catch (Exception e) {
      LOGGER.log(Level.WARNING, "Failed to load sound: " + name, e);
    }
  }

  /**
   * Plays a sound by its name if it exists in the cache and the sound is not muted.
   *
   * @param name the identifier of the sound to play
   */
  public void playSound(String name) {
    if (name == null || name.trim().isEmpty()) {
      LOGGER.warning("Cannot play sound: name is null or empty.");
      return;
    }
    if (muted) {
      return;
    }

    Media media = soundCache.get(name);
    if (media != null) {
      MediaPlayer player = new MediaPlayer(media);
      player.setVolume(volume);
      player.play();

      player.setOnEndOfMedia(player::dispose);
    } else {
      LOGGER.warning("Sound not found: " + name);
    }
  }

  /**
   * Gets the current volume level.
   *
   * @return the current volume level between 0.0 and 1.0
   */
  public double getVolume() {
    return volume;
  }

  /**
   * Sets the volume level for all sounds.
   *
   * @param volume the volume level between 0.0 and 1.0
   * @throws IllegalArgumentException if volume is not between 0.0 and 1.0
   */
  public void setVolume(double volume) {
    if (volume < 0.0 || volume > 1.0) {
      throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
    }
    this.volume = volume;
  }

  /**
   * Toggles the mute state.
   *
   * @return the new mute state (true if muted, false if unmuted)
   */
  public boolean toggleMute() {
    muted = !muted;
    return muted;
  }

  /**
   * Gets the current mute state.
   *
   * @return true if sound is muted, false otherwise
   */
  public boolean isMuted() {
    return muted;
  }

  /**
   * Sets the mute state.
   *
   * @param muted true to mute sound, false to unmute
   */
  public void setMuted(boolean muted) {
    this.muted = muted;
  }
}

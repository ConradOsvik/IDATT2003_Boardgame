package edu.ntnu.stud.boardgame.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

  private static final Logger LOGGER = Logger.getLogger(SoundManager.class.getName());

  private static SoundManager instance;

  private final Map<String, Media> soundCache;

  private double volume = 0.5;
  private boolean muted = false;

  private SoundManager() {
    this.soundCache = new HashMap<>();
    preloadSounds();
  }

  public static synchronized SoundManager getInstance() {
    if (instance == null) {
      instance = new SoundManager();
    }
    return instance;
  }

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

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    if (volume < 0.0 || volume > 1.0) {
      throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
    }
    this.volume = volume;
  }

  public boolean toggleMute() {
    muted = !muted;
    return muted;
  }

  public boolean isMuted() {
    return muted;
  }

  public void setMuted(boolean muted) {
    this.muted = muted;
  }
}
package edu.ntnu.stud.boardgame.view.components;

import edu.ntnu.stud.boardgame.service.SoundManagerService;
import edu.ntnu.stud.boardgame.view.components.builder.ButtonBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.LabelBuilder;
import edu.ntnu.stud.boardgame.view.components.builder.SliderBuilder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A control panel for managing game audio settings. Provides volume control, mute functionality,
 * and sound testing. Extends {@link VBox} to arrange controls vertically.
 *
 * @see SoundManagerService
 * @see VBox
 */
public class AudioControlPanel extends VBox {

  private final SoundManagerService soundManager;
  private final Slider volumeSlider;
  private final Button muteButton;
  private final Label volumeLabel;

  /**
   * Creates a new audio control panel with volume and mute controls.
   *
   * <p>Initializes the panel with current audio settings from {@link SoundManagerService}.
   */
  public AudioControlPanel() {
    this.soundManager = SoundManagerService.getInstance();

    setPadding(new Insets(15));
    setSpacing(10);
    setAlignment(Pos.TOP_CENTER);
    getStyleClass().add("card");

    volumeLabel = new LabelBuilder().text("Volume: 50%").styleClass("text-body-bold").build();

    volumeSlider =
        new SliderBuilder(0, 1, soundManager.getVolume())
            .majorTickUnit(0.5)
            .showTickMarks(true)
            .showTickLabels(false)
            .prefWidth(getWidth() - 40)
            .onChange(
                (obs, oldVal, newVal) -> {
                  soundManager.setVolume(newVal.doubleValue());
                  updateVolumeLabel();
                })
            .build();

    HBox controlButtons = new HBox(10);
    controlButtons.setAlignment(Pos.CENTER);

    muteButton =
        new ButtonBuilder()
            .text(soundManager.isMuted() ? "Unmute" : "Mute")
            .styleClass("btn-secondary")
            .onClick(e -> toggleMute())
            .build();

    Button testButton =
        new ButtonBuilder()
            .text("Test")
            .styleClass("btn-primary")
            .onClick(e -> soundManager.playSound("dice_roll"))
            .build();

    controlButtons.getChildren().addAll(muteButton, testButton);
    Label titleLabel = new LabelBuilder().text("Audio").styleClass("text-h2").build();

    getChildren().addAll(titleLabel, volumeLabel, volumeSlider, controlButtons);
    updateVolumeLabel();
  }

  /** Updates the volume label to reflect the current volume level. */
  private void updateVolumeLabel() {
    if (soundManager.isMuted()) {
      volumeLabel.setText("Volume: Muted");
    } else {
      int volumePercent = (int) (soundManager.getVolume() * 100);
      volumeLabel.setText("Volume: " + volumePercent + "%");
    }
  }

  /** Toggles the mute state and updates the mute button text. */
  private void toggleMute() {
    boolean muted = soundManager.toggleMute();
    muteButton.setText(muted ? "Unmute" : "Mute");
    updateVolumeLabel();
    volumeSlider.setDisable(muted);
  }
}

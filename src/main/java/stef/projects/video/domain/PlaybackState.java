package stef.projects.video.domain;

import javafx.scene.media.MediaPlayer;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlaybackState {

    private double minSpeed;
    private double maxSpeed;
    private double defaultSpeed;

    private double currentSpeed;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private JLabel playbackLabel;
    private JPanel playerButtonPanel;

    public PlaybackState(double minSpeed, double maxSpeed, double defaultSpeed, MediaPlayer mediaPlayer, JLabel playbackLabel, JPanel playerButtonPanel) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.defaultSpeed = defaultSpeed;
        this.mediaPlayer = mediaPlayer;
        this.playbackLabel = playbackLabel;
        this.playerButtonPanel = playerButtonPanel;
        this.currentSpeed = defaultSpeed;
    }

    public void play() {
        isPlaying = true;
        mediaPlayer.play();
    }

    public void pause() {
        isPlaying = false;
        mediaPlayer.pause();
    }

    public void increaseSpeed() {
        if (currentSpeed < maxSpeed) {
            currentSpeed++;
            setSpeed();
        }
    }

    public void decreaseSpeed() {
        if (currentSpeed > minSpeed) {
            currentSpeed--;
            setSpeed();
        }
    }

    private void setSpeed() {
        mediaPlayer.setRate(currentSpeed);
        playbackLabel.setText("Playback speed  x " + currentSpeed);
        playerButtonPanel.repaint();
    }

}

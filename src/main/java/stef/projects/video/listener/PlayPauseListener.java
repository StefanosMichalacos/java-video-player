package stef.projects.video.listener;

import javafx.scene.media.MediaPlayer;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayPauseListener implements ActionListener {

    private JButton playPauseButton;
    private MediaPlayer mediaPlayer;
    private Boolean isPlaying;

    public PlayPauseListener(JButton playPauseButton, MediaPlayer mediaPlayer, Boolean isPlaying) {
        this.playPauseButton = playPauseButton;
        this.mediaPlayer = mediaPlayer;
        this.isPlaying = isPlaying;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!isPlaying) {
            mediaPlayer.play();
            playPauseButton.setText("Pause");
            isPlaying = true;
        } else {
            mediaPlayer.pause();
            playPauseButton.setText("Start");
            isPlaying = false;
        }

    }
}


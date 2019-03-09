package stef.projects.video.listener;

import stef.projects.video.domain.PlaybackState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlaybackSpeedDownListener implements ActionListener {
    private PlaybackState playbackState;

    public PlaybackSpeedDownListener(PlaybackState playbackState) {
        this.playbackState = playbackState;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        playbackState.decreaseSpeed();
    }
}

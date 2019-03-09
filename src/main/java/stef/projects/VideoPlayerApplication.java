package stef.projects;

import stef.projects.video.domain.VideoPlayer;

import javax.swing.JList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VideoPlayerApplication {
    public static void main(String[] args) {
        File balux = new File(Objects.requireNonNull(VideoPlayerApplication.class.getClassLoader().getResource("video-files/Balux.mp4")).getFile());
        File salamina = new File(Objects.requireNonNull(VideoPlayerApplication.class.getClassLoader().getResource("video-files/Salamina.mp4")).getFile());
        File saronikos = new File(Objects.requireNonNull(VideoPlayerApplication.class.getClassLoader().getResource("video-files/Saronikos.mp4")).getFile());
        List<File> files = new ArrayList<>();
        files.add(balux);
        files.add(salamina);
        files.add(saronikos);
        VideoPlayer videoPlayer = new VideoPlayer(files,"Video Player");
        videoPlayer.show();

    }
}

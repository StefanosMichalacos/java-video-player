package stef.projects.video.domain;

//import icon.StretchIcon;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import stef.projects.video.domain.PlaybackState;
import stef.projects.video.listener.PlayPauseListener;
import stef.projects.video.listener.PlaybackSpeedDownListener;
import stef.projects.video.listener.PlaybackSpeedUpListener;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VideoPlayer {

    private List<File> videoFiles;
    private String videoPlayerTitle;
    private double defaultSpeed = 3.0, minSpeed = 1.0, maxSpeed = 8.0;


    private List<JButton> videoFileButtons = new ArrayList<>();
    private PlaybackState playbackState;


    //////////////////////////////////////////////
    private JFrame mainFrame;
    private JMenuBar menuBar;
    private JMenu help;
    private JMenuItem instructions;
    private JPanel backgroundPanel, videoFileButtonsPanel, playerPanel, playerButtonPanel;
    private JButton saronikosButton, salaminaButton, baluxButton, playPauseButton, playbackSpeedUpButton, playbackSpeedDownButton, resetButton, backButton, screenShotbutton;
    private JLabel playbackLabel;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Media video;
    private File file;
    private Scene scene;
    private Slider slider;
    private final Dimension frameSize = new Dimension(1000, 787);
    private Boolean isPlaying = false;
    private int xSize, ySize;
    private static double playbackSpeed = 3.0;

    public VideoPlayer(List<File> videoFiles, String videoPlayerTitle) {
        this.videoFiles = videoFiles;
        this.videoPlayerTitle = videoPlayerTitle;
        constructVideoFileButtons();
        this.videoFileButtonsPanel = extractPanelWithComponents(videoFileButtons);
        this.backgroundPanel = extractPanelWithImageBackground();
        this.mainFrame = new JFrame("Progression of Shore Clean-Up Operation in response to the oil spill from M/T Agia Zoni II");
        this.menuBar = extractMenuBar();
        this.mainFrame.getContentPane().add(BorderLayout.SOUTH, videoFileButtonsPanel);
        this.mainFrame.getContentPane().add(BorderLayout.CENTER, backgroundPanel);
        this.mainFrame.setJMenuBar(menuBar);
        configureMainFrame();
        getScreenSize();
        Platform.setImplicitExit(false);

    }

    private void constructVideoFileButtons() {
        for (File videoFile : videoFiles) {
            String videoFileButtonName = videoFile.getName();
            JButton videoFileButton = extractButtonWithText(videoFileButtonName);
            videoFileButton.addActionListener(new VideoFileButtonListener(videoFile));
            videoFileButtons.add(videoFileButton);
        }

    }

    class VideoFileButtonListener implements ActionListener {
        private File videoFile;

        public VideoFileButtonListener(File videoFile) {
            this.videoFile = videoFile;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            playbackSpeed = defaultSpeed;

            try {
                playerPanel = extractPlaybackPanel(videoFile.toURL().toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            playPauseButton = new JButton("Start");
            playPauseButton.addActionListener(new PlayPauseListener(playPauseButton, mediaPlayer, isPlaying));

            playbackSpeedUpButton = new JButton("Playback Speed >>");
            playbackSpeedDownButton = new JButton("<< Playback Speed");
            constructPlayerButtonPanel(playbackSpeedDownButton, playPauseButton, playbackSpeedUpButton);
            playbackState = new PlaybackState(minSpeed, maxSpeed, defaultSpeed, mediaPlayer, playbackLabel, playerButtonPanel);


            playbackSpeedUpButton.addActionListener(new PlaybackSpeedUpListener(playbackState));
            playbackSpeedDownButton.addActionListener(new PlaybackSpeedDownListener(playbackState));

            resetButton = new JButton("Reset");
            resetButton.addActionListener(new ResetButtonListener());

            backButton = new JButton("Back to menu");
            backButton.addActionListener(new BackButtonListener());

            screenShotbutton = new JButton("Extract Screenshot");
            screenShotbutton.addActionListener(new ScreenShotButton());

            playerButtonPanel.add(Box.createGlue());
            playerButtonPanel.add(resetButton);
            playerButtonPanel.add(screenShotbutton);
            playerButtonPanel.add(backButton);



            mainFrame.remove(backgroundPanel);
            mainFrame.remove(videoFileButtonsPanel);
            menuBar.setVisible(false);
            mainFrame.getContentPane().add(BorderLayout.CENTER, playerPanel);
            mainFrame.getContentPane().add(BorderLayout.SOUTH, playerButtonPanel);
            maximizeFrame();
            mainFrame.revalidate();
            mainFrame.repaint();
        }

    }

    private void constructPlayerButtonPanel(Component... components) {
        playerButtonPanel = new JPanel();
        playerButtonPanel.setLayout(new BoxLayout(playerButtonPanel, BoxLayout.X_AXIS));
        playbackLabel = new JLabel(" Playback speed  x " + playbackSpeed);
        playbackLabel.setFont(new Font("Dialog", 1, 16));
        playerButtonPanel.add(playbackLabel);
        playerButtonPanel.add(Box.createRigidArea(new Dimension(150, 0)));
        playerButtonPanel.add(Box.createGlue());
        for (Component component : components) {
            playerButtonPanel.add(component);
        }
    }


    private JMenuBar extractMenuBar() {

        JMenuBar jMenuBar = new JMenuBar();
        help = new JMenu("Help");
        instructions = new JMenuItem("<html> INSTRUCTIONS  <br> <br> ~ Choose the presentation you want by clicking the corresponding button. <br> <br> ~ To start the presentation click Play. <br> <br> ~ The playback speed is showed on the bottom left and controlled by the playbackSpeed buttons. <br> <br> ~ To extract screenshots press Pause and Extract Screenshot. </html>");
//        instructions.setPreferredSize(new Dimension(500,250));
        help.add(instructions);
        jMenuBar.add(help);
        return jMenuBar;
    }

    private JPanel extractPanelWithComponents(List<? extends Component> components) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
        jPanel.add(Box.createGlue());
        for (Component component : components) {
            jPanel.add(component);
            jPanel.add(Box.createGlue());
        }
        return jPanel;

    }

    private void configureMainFrame() {
        mainFrame.setBackground(Color.black);
        mainFrame.setSize(frameSize);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
    }

    private void getScreenSize() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        xSize = ((int) tk.getScreenSize().getWidth());
        ySize = ((int) tk.getScreenSize().getHeight());

    }


    private JPanel extractPanelWithImageBackground() {
        JPanel jPanel = new JPanel();
        jPanel.setBackground(Color.black);
        jPanel.setLayout(new BorderLayout());
//        StretchIcon pic = new StretchIcon(getClass().getClassLoader().getResource("pic.jpg"));
//        jPanel.add(BorderLayout.CENTER, new JLabel(pic));
        return jPanel;
    }


    private JButton extractButtonWithText(String text) {
        JButton jButton = new JButton(text);
        jButton.setPreferredSize(new Dimension(95, 26));
        return jButton;
    }


    public void show() {
        mainFrame.setVisible(true);
    }

//    private void configureSecondaryPanels(String file) {
//        playPauseButton = new JButton("Start");
//        playbackSpeedUpButton = new JButton("Playback Ratio >>");
//        playbackSpeedDownButton = new JButton("<< Playback Ratio");
//        resetButton = new JButton("Reset");
//        backButton = new JButton("Back to menu");
//        screenShotbutton = new JButton("Extract Screenshot");
//        playPauseButton.addActionListener(new PlayPauseListener());
//        backButton.addActionListener(new BackButtonListener());
//        playbackSpeedUpButton.addActionListener(new PlaybackSpeedUpListener());
//        playbackSpeedDownButton.addActionListener(new RatioDownListener());
//        resetButton.addActionListener(new ResetButtonListener());
//        screenShotbutton.addActionListener(new ScreenShotButton());
//        playerButtonPanel = constructPlayerButtonPanel(playbackSpeedDownButton, playPauseButton, playbackSpeedUpButton);
//        playerButtonPanel.add(Box.createGlue());
//        playerButtonPanel.add(resetButton);
//        playerButtonPanel.add(screenShotbutton);
//        playerButtonPanel.add(backButton);
//        playerPanel = extractPlaybackPanel(file);
//    }

    private JPanel extractPlaybackPanel(String file) {
        JPanel videoPanel = new JPanel(new BorderLayout());
        JFXPanel jfxPanel = new JFXPanel();
        Group sceneGroup = new Group();
        video = new Media(file);
        mediaPlayer = new MediaPlayer(video);
        mediaPlayer.setRate(playbackSpeed);
        mediaView = new MediaView(mediaPlayer);
        DoubleProperty mediaViewWidth = mediaView.fitWidthProperty();
        DoubleProperty mediaViewHeight = mediaView.fitHeightProperty();
        mediaView.setPreserveRatio(true);
        mediaPlayer.setOnReady(() -> {
            int width = mediaPlayer.getMedia().getWidth();
            int height = mediaPlayer.getMedia().getHeight();

            slider = new Slider();
            slider.setTranslateY(height - 65);
            slider.setMinWidth(width);
            slider.setMinHeight(10);
            slider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            slider.setVisible(true);
            sceneGroup.getChildren().add(slider);

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    slider.setValue(newValue.toSeconds());
                }
            });

            slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(slider.getValue()));
                }
            });
        });
        sceneGroup.getChildren().add(mediaView);
        scene = new Scene(sceneGroup, javafx.scene.paint.Color.BLACK); //xSize, ySize,
        mediaViewWidth.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        mediaViewHeight.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        scene.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                slider.setVisible(false);
            }
        });
        scene.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                slider.setVisible(true);
            }
        });

        jfxPanel.setScene(scene);
        jfxPanel.setBackground(Color.BLACK);
        videoPanel.add(BorderLayout.CENTER, jfxPanel);


        return videoPanel;
    }

    class ScreenShotButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    int width = mediaPlayer.getMedia().getWidth();
                    int height = mediaPlayer.getMedia().getHeight();
                    WritableImage wim = new WritableImage(width, height);
                    MediaView mv = new MediaView();
                    mv.setFitWidth(width);
                    mv.setFitHeight(height);
                    mv.setMediaPlayer(mediaPlayer);
                    mv.snapshot(null, wim);

                    try {
                        JFileChooser chooser = new JFileChooser();
                        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                            File renamer = chooser.getSelectedFile();
                            file = new File(chooser.getCurrentDirectory().toString() + "/" + renamer.getName() + ".png");
                        }
                        ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                    } catch (IOException s) {
                        JOptionPane.showMessageDialog(null, "exeption : " + s);
                        System.out.println(s);
                    }
                }
            });
        }

    }

    private void maximizeFrame() {
        mainFrame.setResizable(true);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.dispose();
        mainFrame.setUndecorated(true);
        mainFrame.setVisible(true);
    }


    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            mediaPlayer.stop();
            mainFrame.remove(playerPanel);
            mainFrame.remove(playerButtonPanel);
            menuBar.setVisible(true);
            mainFrame.getContentPane().add(BorderLayout.CENTER, backgroundPanel);
            mainFrame.getContentPane().add(BorderLayout.SOUTH, videoFileButtonsPanel);
            resizeFrame();
            mainFrame.revalidate();
            mainFrame.repaint();
            playPauseButton.setText("Start");
            isPlaying = false;
        }

    }


    class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            mediaPlayer.stop();
            playPauseButton.setText("Start");
            isPlaying = false;
        }
    }

    private void resizeFrame() {
        mainFrame.setSize(frameSize);
        mainFrame.setResizable(false);
        mainFrame.dispose();
        mainFrame.setUndecorated(false);
        mainFrame.setVisible(true);
    }
}


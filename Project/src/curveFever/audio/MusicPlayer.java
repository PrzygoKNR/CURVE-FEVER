package curveFever.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

public class MusicPlayer {
    private final String defaultMusicFolderPath = "/"
            + getClass().getPackage().getName().replaceAll("\\.", "/")
            + "/resources/";

    private File musicFolder;
    private Queue<File> musicList;
    public MediaPlayer mediaPlayer;
    private boolean looping;

    private void playSongsTillMidnight() {
        if (musicList.peek() == null) {
            if(looping) {
                musicPlayerController();
            } else return;
        }
        Media media = new Media(musicList.poll().toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            mediaPlayer.play();
            mediaPlayer.setOnEndOfMedia(this::onEndOfMedia);
        });
    }

    private void onEndOfMedia() {
        mediaPlayer.dispose();
        mediaPlayer = null;
        playSongsTillMidnight();
    }

    private void musicPlayerController() {
        if (musicFolder == null) return;
        if(musicFolder.listFiles() == null) return;

        musicList = new LinkedList<>();
        Arrays.stream(musicFolder.listFiles())
                .filter(file -> file.toString().endsWith(".mp3"))
                .forEach(file -> musicList.add(file));

        if (!musicList.isEmpty()) {
            playSongsTillMidnight();
        }
    }

    public MusicPlayer() {
        looping = false;
        setMusicFolder(defaultMusicFolderPath);
        musicPlayerController();
    }

    public MusicPlayer(File musicFolder) {
        looping = true;
        setMusicFolder(musicFolder);
        musicPlayerController();
    }

    public MusicPlayer(String musicFolder) {
        looping = true;
        setMusicFolder(musicFolder);
        musicPlayerController();
    }

    private void setMusicFolder(String folder) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        try {
            musicFolder = new File(getClass().getResource(folder).toURI());
        } catch (URISyntaxException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Couldn't load audio resources file");
            musicFolder = null;
        }
    }

    private void setMusicFolder(File folder) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        if (!folder.isDirectory()) {
            musicFolder = null;
            throw new IllegalArgumentException("Not a directory");
        }
        musicFolder = folder;
    }

    public void skipSong() {
        if(musicList != null) {
            if (!musicList.isEmpty()) {
                onEndOfMedia();
            } else System.out.println("No songs to skip");
        }
    }

    public void shuffleMusicList() {
        if (musicList != null)
            Collections.shuffle((List<?>) musicList);
    }

    public void addSongToList(File song) {
        if (song.toString().endsWith(".mp3"))
            musicList.add(song);
    }

//    new MusicPlayer()
//    public void toDefaultResourceFolder() {
//        setMusicFolder(defaultMusicFolderPath);
//    }

    public File getMusicFolder() {
        return musicFolder;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }
}

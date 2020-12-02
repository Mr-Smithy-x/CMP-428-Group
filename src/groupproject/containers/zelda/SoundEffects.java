package groupproject.containers.zelda;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundEffects {
    private Clip clip;

    public SoundEffects(String filepath) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filepath));
            AudioFormat format = stream.getFormat();

            // specify what kind of line you want to create
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // create the line
            clip = (Clip)AudioSystem.getLine(info);

            // Load the samples from the stream
            clip.open(stream);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void stop() {
        if (clip.isRunning()) clip.stop();
    }

    public void close() {
        stop();
        clip.close();
    }
}

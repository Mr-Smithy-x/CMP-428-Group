package groupproject.gameengine.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public abstract class BaseSoundEffects {

    private static final String SOUND_EFFECTS_FOLDER = "assets/sounds/effects";

    public Clip createClip(String file){
        return init(get(file));
    }

    protected File get(String file){
        return new File(String.format("%s/%s",SOUND_EFFECTS_FOLDER, file));
    }

    protected boolean exists(String file){
        return get(file).exists();
    }

    public Clip init(File file) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            // specify what kind of line you want to create
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            // create the line
            Clip clip = (Clip)AudioSystem.getLine(info);
            // Load the samples from the stream
            clip.open(stream);
            return clip;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void play(Clip clip) {
        if (clip == null) return;
        if(!clip.isActive()) {
            stop(clip);
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop(Clip clip) {
        if (clip.isRunning()) clip.stop();
    }

    public void close(Clip clip) {
        stop(clip);
        clip.close();
    }
}

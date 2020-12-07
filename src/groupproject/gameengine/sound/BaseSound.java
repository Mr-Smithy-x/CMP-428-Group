package groupproject.gameengine.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public abstract class BaseSound {

    protected static final String SOUND_TRACK_FOLDER = "assets/sound/tracks";
    protected static final String SOUND_EFFECTS_FOLDER = "assets/sound/effects";

    public static Clip init(File file) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            // specify what kind of line you want to create
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            // create the line
            Clip clip = (Clip) AudioSystem.getLine(info);
            // Load the samples from the stream
            clip.open(stream);
            return clip;
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract String getAssetFolder();

    public Clip createClip(String file) {
        return init(get(file));
    }

    public float getVolume(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(Clip clip, float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    protected File get(String file) {
        return new File(String.format("%s/%s", getAssetFolder(), file));
    }

    protected boolean exists(String file) {
        return get(file).exists();
    }

    public void play(Clip clip) {
        if (clip == null) return;
        setVolume(clip, 0.2f);
        boolean active = clip.isActive();
        if (!active) {
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

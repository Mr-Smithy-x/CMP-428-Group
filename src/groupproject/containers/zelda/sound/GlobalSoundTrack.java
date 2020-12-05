package groupproject.containers.zelda.sound;

import groupproject.gameengine.sound.BaseSound;

import javax.sound.sampled.Clip;
import java.util.EnumMap;

public class GlobalSoundTrack extends BaseSound {

    private static GlobalSoundTrack instance;
    private final EnumMap<Track, Clip> clips;
    protected Track track = Track.NORMAL;

    private GlobalSoundTrack() {
        clips = new EnumMap<>(Track.class);
        for (Track value : Track.values()) {
            if (exists(value.value)) {
                Clip clip = createClip(value.value);
                clips.put(value, clip);
            }
        }
    }

    public static GlobalSoundTrack getInstance() {
        if (instance == null) {
            instance = new GlobalSoundTrack();
        }
        return instance;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        if (this.track != track) {
            Clip clip = this.clips.get(this.track);
            if (clip.isActive()) {
                stop(clip);
            }
            this.track = track;
        }
    }

    public void play() {
        play(clips.get(track));
    }

    @Override
    public String getAssetFolder() {
        return SOUND_TRACK_FOLDER;
    }

    public enum Track {
        PAUSE("select_screen.wav"),
        NORMAL("overworld.wav"),
        COMBAT("forest.wav"),
        DUNGEON("sanctuary_dungeon.wav");

        private final String value;

        Track(String value) {
            this.value = value;
        }

    }

}

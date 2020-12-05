package groupproject.gameengine.sound;

import javax.sound.sampled.Clip;
import java.util.EnumMap;

public class GlobalSoundTrack extends BaseSound {

    protected Track track = Track.NORMAL;
    private final EnumMap<Track, Clip> clips;

    private static GlobalSoundTrack instance;

    private GlobalSoundTrack() {
        clips = new EnumMap<>(Track.class);
        for (Track value : Track.values()) {
            if(exists(value.track)) {
                Clip clip = createClip(value.track);
                clips.put(value, clip);
            }
        }
    }

    public void setTrack(Track track) {
        if(this.track != track) {
            Clip clip = this.clips.get(this.track);
            if (clip.isActive()) {
                stop(clip);
            }
            this.track = track;
        }
    }

    public Track getTrack() {
        return track;
    }

    public static GlobalSoundTrack getInstance() {
        if (instance == null) {
            instance = new GlobalSoundTrack();
        }
        return instance;
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

        private final String track;

        Track(String track) {
            this.track = track;
        }

    }

}

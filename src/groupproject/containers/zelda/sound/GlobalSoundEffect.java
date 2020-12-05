package groupproject.containers.zelda.sound;

import groupproject.gameengine.sound.BaseSound;
import groupproject.gameengine.sprite.Sprite;

import javax.sound.sampled.Clip;
import java.util.EnumMap;

public class GlobalSoundEffect extends BaseSound {

    private final EnumMap<Sprite.Pose, Clip> clips;

    private static GlobalSoundEffect instance;

    private GlobalSoundEffect() {
        clips = new EnumMap<>(Sprite.Pose.class);
        Clips.HURT = init(get("hurt.wav"));
        Clips.FALL = init(get("fall.wav"));
        Clips.ERROR = init(get("error.wav"));
        Sprite.Pose[] values = Sprite.Pose.values();
        for (Sprite.Pose value : values) {
            if (value.hasSoundFile() && exists(value.getSoundFileName())) {
                clips.put(value, createClip(value.getSoundFileName()));
            }
        }
    }

    public static GlobalSoundEffect getInstance() {
        if (instance == null) {
            instance = new GlobalSoundEffect();
        }
        return instance;
    }

    public void play(Sprite sprite) {
        Sprite.Pose spritePose = sprite.getSpritePose();
        if (clips.containsKey(spritePose) && sprite.isActive()) {
            play(clips.get(spritePose));
        }
    }

    @Override
    public String getAssetFolder() {
        return SOUND_EFFECTS_FOLDER;
    }

    public static class Clips {
        public static Clip FALL = null;
        public static Clip HURT = null;
        public static Clip ERROR = null;
    }
}

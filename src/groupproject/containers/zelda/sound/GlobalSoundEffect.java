package groupproject.containers.zelda.sound;

import groupproject.gameengine.sound.BaseSound;
import groupproject.gameengine.sprite.Sprite;

import javax.sound.sampled.Clip;
import java.util.EnumMap;

public class GlobalSoundEffect extends BaseSound {

    private static GlobalSoundEffect instance;
    private final EnumMap<Sprite.Pose, Clip> clips;

    private GlobalSoundEffect() {
        clips = new EnumMap<>(Sprite.Pose.class);

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
            Clip clip = clips.get(spritePose);
            if(sprite.getCurrentAnimation().isFirstFrame()){
                play(clip, false);
            }
        }
    }

    @Override
    public String getAssetFolder() {
        return SOUND_EFFECTS_FOLDER;
    }

    /**
     * Add Aditional clips in the event you want to play custom clips
     */
    public static class Clips {
        public static final Clip FALL = init(getInstance().get("hurt.wav"));
        public static final Clip HURT = init(getInstance().get("fall.wav"));
        public static final Clip ERROR = init(getInstance().get("error.wav"));
        public static final Clip CANE = init(getInstance().get("cane.wav"));

        private Clips() {
        }
    }
}

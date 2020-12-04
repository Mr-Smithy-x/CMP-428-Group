package groupproject.gameengine.sound;

import groupproject.gameengine.sprite.Sprite;

import javax.sound.sampled.Clip;
import java.util.HashMap;

public class GlobalPoseSoundBoard extends BaseSoundEffects {

    HashMap<Sprite.Pose, Clip> clips = new HashMap<>();

    private static GlobalPoseSoundBoard instance;


    private GlobalPoseSoundBoard() {
        Sprite.Pose[] values = Sprite.Pose.values();
        for (Sprite.Pose value : values) {
            if (exists(value.soundeffect())) {
                clips.put(value, createClip(value.soundeffect()));
            }
        }
    }

    public static GlobalPoseSoundBoard getInstance() {
        if(instance == null){
           instance = new GlobalPoseSoundBoard();
        }
        return instance;
    }

    public void play(Sprite sprite) {
        Sprite.Pose spritePose = sprite.getSpritePose();
        if (clips.containsKey(spritePose) && sprite.isActive()) {
            play(clips.get(spritePose));
        }
    }
}

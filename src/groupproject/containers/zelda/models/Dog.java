package groupproject.containers.zelda.models;

import groupproject.gameengine.sprite.Animation;
import groupproject.gameengine.sprite.Sprite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;

public class Dog extends Sprite {

    public Dog(int positionX, int positionY, int duration) throws IOException {
        super("dog.png", positionX, positionY, 3, duration);
    }

    @Override
    protected EnumMap<Pose, Animation> setupImages(BufferedImage image, int delay) {
        EnumMap<Pose, Animation> map = new EnumMap<>(Pose.class);
        map.put(Pose.DOWN, getAnimation(image, 0, 0, 32, 32, 4, delay));
        map.put(Pose.RIGHT, getAnimation(image, 0, 1, 32, 32, 4, delay));
        map.put(Pose.UP, getAnimation(image, 0, 2, 32, 32, 4, delay));
        map.put(Pose.LEFT, getAnimation(image, 0, 3, 32, 32, 4, delay));
        return map;
    }

    @Override
    protected void initAnimations() {
        animDict.values().forEach(a -> a.scale(scaled));
    }
}

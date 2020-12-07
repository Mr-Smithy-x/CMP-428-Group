package groupproject.containers.zelda.models;

import groupproject.gameengine.sprite.Sprite;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.io.IOException;

public class Dog extends Sprite {

    public Dog(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load("dog.pose"), positionX, positionY, 2, duration);
    }

    @Override
    protected void initAnimations() {
        animDict.values().forEach(a -> a.scale(scaled));
    }
}

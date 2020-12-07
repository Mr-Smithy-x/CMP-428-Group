package groupproject.containers.zelda.models;


import groupproject.gameengine.sprite.AttackSprite;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.io.IOException;

public class Gibdo extends AttackSprite {

    public Gibdo(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load(Gibdo.class.getSimpleName().toLowerCase()), positionX, positionY, 2, duration);
    }

}

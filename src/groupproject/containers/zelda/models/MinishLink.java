package groupproject.containers.zelda.models;

import groupproject.gameengine.sprite.AttackSprite;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.io.IOException;

public class MinishLink extends AttackSprite {

    public MinishLink(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load("link_final_spritesheet"), positionX, positionY, 2, duration);
    }

}

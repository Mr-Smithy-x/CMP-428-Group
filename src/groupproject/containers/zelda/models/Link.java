package groupproject.containers.zelda.models;


import groupproject.gameengine.sprite.AttackSprite;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.io.IOException;

public class Link extends AttackSprite {

    public Link(int positionX, int positionY, int duration) throws IOException {
        super(PoseFileFormat.Companion.load("link.pose"), positionX, positionY, 2, duration);
    }

}

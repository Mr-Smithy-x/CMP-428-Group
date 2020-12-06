package groupproject.containers.pacman.models;

import groupproject.gameengine.models.BoundingBox;
import groupproject.gameengine.sprite.Sprite;

public class Ghost extends Sprite {

    public Ghost(int x, int y, String spritePrefix, int delay) {
        super(x, y, spritePrefix, delay);
        this.bounds = new BoundingBox(x + 4, y + 5, 8, 8);
    }

    @Override
    protected void initAnimations() {
        /*
         * Not needed currently.
         */
    }
}

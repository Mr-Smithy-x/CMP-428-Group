package groupproject.games;

import groupproject.gameengine.sprite.SpriteSheet;
import groupproject.gameengine.sprite.SubImage;

import java.io.IOException;

public class Dog extends SpriteSheet {

    int speed = 3;

    public Dog(int position_x, int position_y, int duration) throws IOException {
        super();
        this.duration = duration;
        setWorld(position_x, position_y);
    }
    @Override
    protected void setupImages() {
        this.subImages = new SubImage[16][];
        this.stillImages = new SubImage[16];

        subImages[DOWN] = initAnimation(0, 0, 32, 32, 4);
        subImages[RIGHT] = initAnimation(0, 1, 32, 32, 4);
        subImages[UP] = initAnimation(0, 2, 32, 32, 4);
        subImages[LEFT] = initAnimation(0, 3, 32, 32, 4);

        stillImages[UP] = subImages[UP][0];
        stillImages[DOWN] = subImages[DOWN][0];
        stillImages[LEFT] = subImages[LEFT][0];
        stillImages[RIGHT] = subImages[RIGHT][0];
    }

}

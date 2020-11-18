package groupproject.containers.zelda.models;


import groupproject.gameengine.sprite.Animation;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.sprite.SpriteSheet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Link extends Sprite {

    boolean attacking = false;

    public Link(int position_x, int position_y, int duration) throws IOException {
        super("link.png", position_x, position_y, 3, duration);
    }

    public void hit(Sprite obj) {
        double speed = 2000;
        double cosAngle = speed * getCosAngle().doubleValue();
        double sinAngle = speed * getSinAngle().doubleValue();
        switch (getSpriteDirection()) {
            case UP:
                sinAngle *= 1;
                cosAngle = 0;
                break;
            case DOWN:
                sinAngle *= -1;
                cosAngle = 0;
                break;
            case LEFT:
                sinAngle = 0;
                cosAngle *= -1;
                break;
            case RIGHT:
                sinAngle = 0;
                cosAngle *= 1;
                break;
        }
        obj.setVelocity(cosAngle, sinAngle);
    }

    public void attack(ArrayList<SpriteSheet> objects) {
        this.attack();
    }

    @Override
    protected HashMap<Direction, Animation> setupImages(BufferedImage image, int delay) {
        HashMap<Direction, Animation> map = super.setupImages(image, delay);
        //this.subImages = new SubImage[16][];
        //this.stillImages = new SubImage[16];
        map.put(Direction.UP, getAnimation(image, 0, 4, 30, 30, 8, delay));
        map.put(Direction.DOWN, getAnimation(image, 0, 1, 30, 30, 8, delay));
        map.put(Direction.LEFT, getAnimation(image, 8, 1, 30, 30, 6, delay));
        map.put(Direction.RIGHT, getAnimation(image, 8, 4, 30, 30, 6, delay));

        map.get(Direction.UP).setFirstFrame(pluck(image, 2, 0, 30, 30));
        map.get(Direction.DOWN).setFirstFrame(pluck(image, 1, 0, 30, 30));
        map.get(Direction.LEFT).setFirstFrame(pluck(image, 5, 0, 30, 30));
        map.get(Direction.RIGHT).setFirstFrame(pluck(image, 11, 4, 30, 30));


        /*
        subImages[ATTACK_UP] = new SubImage[]{
                new SubImage(0, 180, 22, 25),
                new SubImage(30, 177, 22, 30),
                new SubImage(61, 174, 20, 35),
                new SubImage(89, 177, 24, 30),
        };
        subImages[ATTACK_DOWN] = new SubImage[]{
                new SubImage(0, 90, 21, 23),
                new SubImage(30, 90, 22, 23),
                new SubImage(61, 85, 20, 32),
                new SubImage(91, 85, 20, 32),
                new SubImage(115, 87, 28, 29),
        };

        subImages[ATTACK_LEFT] = new SubImage[]{
                new SubImage(242, 90, 260 - 242, 23),
                new SubImage(268, 90, 294 - 268, 24),
                new SubImage(295, 91, 326 - 295, 21),
                new SubImage(327, 91, 355 - 327, 21),
        };

        subImages[ATTACK_RIGHT] = new SubImage[]{

                new SubImage(242, 180, 260 - 242, 23),
                new SubImage(268, 180, 294 - 268, 24),
                new SubImage(295, 181, 326 - 295, 21),
                new SubImage(327, 181, 355 - 327, 21)
        };

        subImages[SPIN_ATTACK] = new SubImage[]{
                new SubImage(115, 180, 32, 23), // Up
                new SubImage(359, 86, 382 - 359, 31), //LEFT,
                new SubImage(145, 88, 31, 27), // Down
                new SubImage(359, 176, 382 - 359, 31), // RIGHT
        };

                /*subImages[ATTACK_UP] = initAnimation(0, 6, 30, 30, 5);
                subImages[ATTACK_DOWN] = initAnimation(0, 3, 28, 28, 6);
                subImages[ATTACK_LEFT] = initAnimation(8, 3, 29, 30, 5);
                subImages[ATTACK_RIGHT] = initAnimation(8, 6, 29, 30, 5);

        stillImages[ATTACK_UP] = initAnimation(2, 0, 30, 30, 1)[0];
        stillImages[ATTACK_DOWN] = initAnimation(1, 0, 30, 30, 1)[0];
        stillImages[ATTACK_LEFT] = initAnimation(5, 0, 30, 30, 1)[0];
        stillImages[ATTACK_RIGHT] = initAnimation(11, 4, 30, 30, 1)[0];
        stillImages[SPIN_ATTACK] = stillImages[DOWN];

        */
        return map;
    }

    public void attack() {
        attacking = true;
        switch (getSpriteDirection()) {
            case UP:
                currentDirection = Direction.UP;
                break;
            case LEFT:
                currentDirection = Direction.LEFT;
                break;
            case RIGHT:
                currentDirection = Direction.RIGHT;
                break;
            case DOWN:
                currentDirection = Direction.DOWN;
                break;
        }
    }


    @Override
    protected void initAnimations() {
        animDict.values().forEach( a -> a.scale(scaled));
    }

}

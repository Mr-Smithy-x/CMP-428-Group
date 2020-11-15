package groupproject.games;


import groupproject.gameengine.sprite.SpriteSheet;
import groupproject.gameengine.sprite.SubImage;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Link extends SpriteSheet {

    boolean attacking = false;

    public Link() throws IOException {
        this(200, 200, 1);
    }

    public Link(int duration) throws IOException {
        this(200, 200, duration);
    }

    public Link(int position_x, int position_y, int duration) throws IOException {
        super();
        shift_right = 12;
        shift_down = 6;
        this.duration = duration;
        setWorld(position_x, position_y);
    }

    public void spin() {
        attacking = true;
        pose = SPIN_ATTACK;
        nextImageColumn();
    }


    public void hit(SpriteSheet obj) {
        double speed = 2000;
        double cosAngle = speed * getCosAngle().doubleValue();
        double sinAngle = speed * getSinAngle().doubleValue();

        switch (getPose()) {
            case ATTACK_UP:
                sinAngle *= 1;
                cosAngle = 0;
                break;
            case ATTACK_DOWN:
                sinAngle *= -1;
                cosAngle = 0;
                break;
            case ATTACK_LEFT:
                sinAngle = 0;
                cosAngle *= -1;
                break;
            case ATTACK_RIGHT:
                sinAngle = 0;
                cosAngle *= 1;
                break;
        }
        obj.setVelocity(cosAngle, sinAngle);
    }

    public void attack(ArrayList<SpriteSheet> objects) {
        this.attack();
    }

    public void attack() {
        attacking = true;
        switch (pose) {
            case UP:
                pose = ATTACK_UP;
                break;
            case LEFT:
                pose = ATTACK_LEFT;
                break;
            case RIGHT:
                pose = ATTACK_RIGHT;
                break;
            case DOWN:
            case SPIN_ATTACK:
                pose = ATTACK_DOWN;
                break;
        }
        nextImageColumn();
    }


    @Override
    protected void setupImages() {
        this.subImages = new SubImage[16][];
        this.stillImages = new SubImage[16];
        subImages[UP] = initAnimation(0, 4, 30, 30, 8);
        subImages[DOWN] = initAnimation(0, 1, 30, 30, 8);
        subImages[LEFT] = initAnimation(8, 1, 30, 30, 6);
        subImages[RIGHT] = initAnimation(8, 4, 30, 30, 6);
        stillImages[UP] = initAnimation(2, 0, 30, 30, 1)[0];
        stillImages[DOWN] = initAnimation(1, 0, 30, 30, 1)[0];
        stillImages[LEFT] = initAnimation(5, 0, 30, 30, 1)[0];
        stillImages[RIGHT] = initAnimation(11, 4, 30, 30, 1)[0];


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
                subImages[ATTACK_RIGHT] = initAnimation(8, 6, 29, 30, 5);*/

        stillImages[ATTACK_UP] = initAnimation(2, 0, 30, 30, 1)[0];
        stillImages[ATTACK_DOWN] = initAnimation(1, 0, 30, 30, 1)[0];
        stillImages[ATTACK_LEFT] = initAnimation(5, 0, 30, 30, 1)[0];
        stillImages[ATTACK_RIGHT] = initAnimation(11, 4, 30, 30, 1)[0];
        stillImages[SPIN_ATTACK] = stillImages[DOWN];
    }

    @Override
    public void render(Graphics g) {
        if (attacking) {
            moving = attacking;
        }
        super.render(g);
        attacking = false;
    }

}

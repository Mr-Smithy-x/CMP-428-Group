package groupproject.containers.zelda.models;


import groupproject.gameengine.sprite.Animation;
import groupproject.gameengine.sprite.Sprite;

import java.awt.*;
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

    public void attack(ArrayList<Sprite> objects) {
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


        Animation attackUP = Animation.with(delay);
        attackUP.addFrame(image.getSubimage(0, 180, 22, 25));
        attackUP.addFrame(image.getSubimage(30, 177, 22, 30));
        attackUP.addFrame(image.getSubimage(61, 174, 20, 35));
        attackUP.addFrame(image.getSubimage(89, 177, 24, 30));
        attackUP.setFirstFrame(map.get(Direction.UP).getFirstFrame());
        map.put(Direction.ATTACK_UP, attackUP);


        Animation attackDown = Animation.with(delay);
        attackDown.addFrame(image.getSubimage(0, 90, 21, 23));
        attackDown.addFrame(image.getSubimage(30, 90, 22, 23));
        attackDown.addFrame(image.getSubimage(61, 85, 20, 32));
        attackDown.addFrame(image.getSubimage(91, 85, 20, 32));
        attackDown.addFrame(image.getSubimage(115, 87, 28, 29));
        attackDown.setFirstFrame(map.get(Direction.DOWN).getFirstFrame());
        map.put(Direction.ATTACK_DOWN, attackDown);


        Animation attackLeft = Animation.with(delay);
        attackLeft.addFrame(image.getSubimage(242, 90, 260 - 242, 23));
        attackLeft.addFrame(image.getSubimage(268, 90, 294 - 268, 24));
        attackLeft.addFrame(image.getSubimage(295, 91, 326 - 295, 21));
        attackLeft.addFrame(image.getSubimage(327, 91, 355 - 327, 21));
        attackLeft.setFirstFrame(map.get(Direction.LEFT).getFirstFrame());
        map.put(Direction.ATTACK_LEFT, attackLeft);


        Animation attackRight = Animation.with(delay);
        attackRight.addFrame(image.getSubimage(242, 180, 260 - 242, 23));
        attackRight.addFrame(image.getSubimage(268, 180, 294 - 268, 24));
        attackRight.addFrame(image.getSubimage(295, 181, 326 - 295, 21));
        attackRight.addFrame(image.getSubimage(327, 181, 355 - 327, 21));
        attackRight.setFirstFrame(map.get(Direction.RIGHT).getFirstFrame());
        map.put(Direction.ATTACK_RIGHT, attackRight);


        Animation spinAttack = Animation.with(delay);
        spinAttack.addFrame(image.getSubimage(115, 180, 32, 23));
        spinAttack.addFrame(image.getSubimage(359, 86, 382 - 359, 31));
        spinAttack.addFrame(image.getSubimage(145, 88, 31, 27));
        spinAttack.addFrame(image.getSubimage(359, 176, 382 - 359, 31));
        spinAttack.setFirstFrame(map.get(Direction.UP).getFirstFrame());
        map.put(Direction.SPIN_ATTACK, spinAttack);





                /*subImages[ATTACK_UP] = initAnimation(0, 6, 30, 30, 5);
                subImages[ATTACK_DOWN] = initAnimation(0, 3, 28, 28, 6);
                subImages[ATTACK_LEFT] = initAnimation(8, 3, 29, 30, 5);
                subImages[ATTACK_RIGHT] = initAnimation(8, 6, 29, 30, 5);*/
/*
        stillImages[ATTACK_UP] = initAnimation(2, 0, 30, 30, 1)[0];
        stillImages[ATTACK_DOWN] = initAnimation(1, 0, 30, 30, 1)[0];
        stillImages[ATTACK_LEFT] = initAnimation(5, 0, 30, 30, 1)[0];
        stillImages[ATTACK_RIGHT] = initAnimation(11, 4, 30, 30, 1)[0];
        stillImages[SPIN_ATTACK] = stillImages[DOWN];*/

        return map;
    }

    public void attack() {
        attacking = true;
        switch (getSpriteDirection()) {
            case UP:
                currentDirection = Direction.ATTACK_UP;
                break;
            case LEFT:
                currentDirection = Direction.ATTACK_LEFT;
                break;
            case RIGHT:
                currentDirection = Direction.ATTACK_RIGHT;
                break;
            case SPIN_ATTACK:
            case DOWN:
                currentDirection = Direction.ATTACK_DOWN;
                break;
        }
    }


    @Override
    public void render(Graphics g) {
        if (attacking) {
            moving = attacking;
        }
        super.render(g);
        attacking = false;
    }

    @Override
    protected void initAnimations() {
        animDict.values().forEach(a -> a.scale(scaled));
    }

    public void spin() {
        attacking = true;
        setSpriteDirection(Direction.SPIN_ATTACK);
    }
}

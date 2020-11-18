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

    /*
    This can be reimplemented when actually needed, base it off the tile map rather than angles.
    public void hit(Sprite obj) {
        double speed = 2000;
        double cosAngle = speed * getCosAngle().doubleValue();
        double sinAngle = speed * getSinAngle().doubleValue();
        switch (getSpritePose()) {
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
    }*/

    public void attack(ArrayList<Sprite> objects) {
        this.attack();
    }

    public void attack() {
        attacking = true;
        switch (getSpritePose()) {
        case UP:
            currentPose = Pose.ATTACK_UP;
            break;
        case LEFT:
            currentPose = Pose.ATTACK_LEFT;
            break;
        case RIGHT:
            currentPose = Pose.ATTACK_RIGHT;
            break;
        case SPIN_ATTACK:
        case DOWN:
            currentPose = Pose.ATTACK_DOWN;
            break;
        }
    }

    @Override
    protected HashMap<Pose, Animation> setupImages(BufferedImage image, int delay) {
        HashMap<Pose, Animation> map = super.setupImages(image, delay);
        //this.subImages = new SubImage[16][];
        //this.stillImages = new SubImage[16];
        map.put(Pose.UP, getAnimation(image, 0, 4, 30, 30, 8, delay));
        map.put(Pose.DOWN, getAnimation(image, 0, 1, 30, 30, 8, delay));
        map.put(Pose.LEFT, getAnimation(image, 8, 1, 30, 30, 6, delay));
        map.put(Pose.RIGHT, getAnimation(image, 8, 4, 30, 30, 6, delay));

        map.get(Pose.UP).setFirstFrame(pluck(image, 2, 0, 30, 30));
        map.get(Pose.DOWN).setFirstFrame(pluck(image, 1, 0, 30, 30));
        map.get(Pose.LEFT).setFirstFrame(pluck(image, 5, 0, 30, 30));
        map.get(Pose.RIGHT).setFirstFrame(pluck(image, 11, 4, 30, 30));


        Animation attackUP = Animation.with(delay);
        attackUP.addFrame(image.getSubimage(0, 180, 22, 25));
        attackUP.addFrame(image.getSubimage(30, 177, 22, 30));
        attackUP.addFrame(image.getSubimage(61, 174, 20, 35));
        attackUP.addFrame(image.getSubimage(89, 177, 24, 30));
        attackUP.setFirstFrame(map.get(Pose.UP).getFirstFrame());
        map.put(Pose.ATTACK_UP, attackUP);


        Animation attackDown = Animation.with(delay);
        attackDown.addFrame(image.getSubimage(0, 90, 21, 23));
        attackDown.addFrame(image.getSubimage(30, 90, 22, 23));
        attackDown.addFrame(image.getSubimage(61, 85, 20, 32));
        attackDown.addFrame(image.getSubimage(91, 85, 20, 32));
        attackDown.addFrame(image.getSubimage(115, 87, 28, 29));
        attackDown.setFirstFrame(map.get(Pose.DOWN).getFirstFrame());
        map.put(Pose.ATTACK_DOWN, attackDown);


        Animation attackLeft = Animation.with(delay);
        attackLeft.addFrame(image.getSubimage(242, 90, 260 - 242, 23));
        attackLeft.addFrame(image.getSubimage(268, 90, 294 - 268, 24));
        attackLeft.addFrame(image.getSubimage(295, 91, 326 - 295, 21));
        attackLeft.addFrame(image.getSubimage(327, 91, 355 - 327, 21));
        attackLeft.setFirstFrame(map.get(Pose.LEFT).getFirstFrame());
        map.put(Pose.ATTACK_LEFT, attackLeft);


        Animation attackRight = Animation.with(delay);
        attackRight.addFrame(image.getSubimage(242, 180, 260 - 242, 23));
        attackRight.addFrame(image.getSubimage(268, 180, 294 - 268, 24));
        attackRight.addFrame(image.getSubimage(295, 181, 326 - 295, 21));
        attackRight.addFrame(image.getSubimage(327, 181, 355 - 327, 21));
        attackRight.setFirstFrame(map.get(Pose.RIGHT).getFirstFrame());
        map.put(Pose.ATTACK_RIGHT, attackRight);


        Animation spinAttack = Animation.with(delay);
        spinAttack.addFrame(image.getSubimage(115, 180, 32, 23));
        spinAttack.addFrame(image.getSubimage(359, 86, 382 - 359, 31));
        spinAttack.addFrame(image.getSubimage(145, 88, 31, 27));
        spinAttack.addFrame(image.getSubimage(359, 176, 382 - 359, 31));
        spinAttack.setFirstFrame(map.get(Pose.UP).getFirstFrame());
        map.put(Pose.SPIN_ATTACK, spinAttack);
        return map;
    }

    @Override
    protected void initAnimations() {
        animDict.values().forEach(a -> a.scale(scaled));
    }

    @Override
    public void render(Graphics g) {
        if (attacking) {
            moving = attacking;
        }
        super.render(g);
        attacking = false;
    }

    public void spin() {
        attacking = true;
        setSpritePose(Pose.SPIN_ATTACK);
    }
}

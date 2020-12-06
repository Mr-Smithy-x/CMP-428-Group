package groupproject.containers.zelda.models;


import groupproject.containers.zelda.contracts.Energy;
import groupproject.gameengine.sprite.Animation;
import groupproject.gameengine.sprite.Sprite;
import groupproject.spritesheeteditor.models.FileFormat;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;

public class Link extends Sprite implements Energy {

    boolean attacking = false;
    private double health;
    private double energy;

    public Link(int positionX, int positionY, int duration) throws IOException {
        super(FileFormat.Companion.load("link.pose"), positionX, positionY, 2, duration);
    }

    public void attack(List<Sprite> objects) {
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
            default:
                break;
        }
    }

    @Override
    protected EnumMap<Pose, Animation> setupImages(BufferedImage image, int delay) {
        EnumMap<Pose, Animation> map = new EnumMap<>(Pose.class);
        map.put(Pose.UP, parseAnimation(image, 0, 4, 30, 30, 8, delay));
        map.put(Pose.DOWN, parseAnimation(image, 0, 1, 30, 30, 8, delay));
        map.put(Pose.LEFT, parseAnimation(image, 8, 1, 30, 30, 6, delay));
        map.put(Pose.RIGHT, parseAnimation(image, 8, 4, 30, 30, 6, delay));

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

        Animation dead = Animation.with(delay);
        dead.addFrame(pluck(image, 4, 7, 30, 30));
        map.put(Pose.DEAD, dead);
        return map;
    }


    @Override
    protected void initAnimations() {
        animDict.values().forEach(a -> a.scale(scaled));
    }

    @Override
    public void render(Graphics g) {
        if (isDead()) {
            setSpritePose(Pose.DEAD);
        } else if (currentPose == Pose.DEAD) {
            setSpritePose(Pose.DOWN);
        }
        if (attacking) {
            moving = attacking;
        }
        super.render(g);
    }

    public void spin() {
        attacking = true;
        setSpritePose(Pose.SPIN_ATTACK);
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public double getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(double energy) {
        this.energy = energy;
    }

}

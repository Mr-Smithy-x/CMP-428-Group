package groupproject.gameengine.sprite;


import groupproject.containers.zelda.contracts.Energy;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public abstract class AttackSprite extends Sprite implements Energy {

    private double health;
    private double energy;

    protected AttackSprite(PoseFileFormat format, int x, int y, int scaled, int delay) throws IOException {
        super(format, x, y, scaled, delay);
    }

    public AttackSprite(String spriteSheet, int x, int y, int scaled, int delay) throws IOException {
        super(spriteSheet, x, y, scaled, delay);
    }

    public AttackSprite(int x, int y, String spritePrefix, int delay) {
        super(x, y, spritePrefix, delay);
    }

    public void attack(List<Sprite> objects) {
        this.attack();
    }

    @Override
    public boolean shoot() {
        canePose();
        boolean shoot = super.shoot();
        if (shoot) {
            useEnergy(.5);
        }
        return shoot;
    }

    public void canePose() {
        moving = true;
        switch (currentPose.getDirection()) {
            case UP:
                setSpritePose(Pose.ATTACK_UP_01);
                break;
            case NONE:
            case DOWN:
                setSpritePose(Pose.ATTACK_DOWN_01);
                break;
            case LEFT:
                setSpritePose(Pose.ATTACK_LEFT_01);
                break;
            case RIGHT:
                setSpritePose(Pose.ATTACK_RIGHT_01);
                break;
        }
    }

    public void roll() {
        moving = true;
        switch (getSpritePose().getDirection()) {
            case UP:
                setSpritePose(Pose.ROLL_UP);
                break;
            case LEFT:
                setSpritePose(Pose.ROLL_LEFT);
                break;
            case RIGHT:
                setSpritePose(Pose.ROLL_RIGHT);
                break;
            case NONE:
            case DOWN:
                setSpritePose(Pose.ROLL_DOWN);
                break;
            default:
                break;
        }
    }

    public void attack() {
        moving = true;
        switch (getSpritePose().getDirection()) {
            case UP:
                setSpritePose(Pose.ATTACK_UP);
                break;
            case LEFT:
                setSpritePose(Pose.ATTACK_LEFT);
                break;
            case RIGHT:
                setSpritePose(Pose.ATTACK_RIGHT);
                break;
            case DOWN:
            case NONE:
                setSpritePose(Pose.ATTACK_DOWN);
                break;
            default:
                break;
        }
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
        super.render(g);
    }

    public void spin() {
        moving = true;
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

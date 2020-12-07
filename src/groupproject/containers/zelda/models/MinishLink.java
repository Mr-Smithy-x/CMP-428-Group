package groupproject.containers.zelda.models;


import groupproject.containers.zelda.contracts.Energy;
import groupproject.gameengine.sprite.Sprite;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MinishLink extends Sprite implements Energy {

    private double health;
    private double energy;

    public MinishLink(int positionX, int positionY, int duration) throws IOException {
        super(Objects.requireNonNull(PoseFileFormat.Companion.load("link_final_spritesheet.pose")), positionX, positionY, 2, duration);
    }

    public void attack(List<Sprite> objects) {
        this.attack();
    }

    public void roll() {
        moving = true;
        switch (getSpritePose()) {
            case UP:
                currentPose = Pose.ROLL_UP;
                break;
            case LEFT:
                currentPose = Pose.ROLL_LEFT;
                break;
            case RIGHT:
                currentPose = Pose.ROLL_RIGHT;
                break;
            case SPIN_ATTACK:
            case DOWN:
                currentPose = Pose.ROLL_DOWN;
                break;
            default:
                break;
        }
    }

    public void attack() {
        moving = true;
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
    protected void initAnimations() {
        animDict.get(Pose.ATTACK_LEFT).setFirstFrame(animDict.get(Pose.LEFT).getFirstFrame());
        animDict.get(Pose.ATTACK_RIGHT).setFirstFrame(animDict.get(Pose.RIGHT).getFirstFrame());
        animDict.get(Pose.ATTACK_DOWN).setFirstFrame(animDict.get(Pose.DOWN).getFirstFrame());
        animDict.get(Pose.ATTACK_UP).setFirstFrame(animDict.get(Pose.UP).getFirstFrame());
        animDict.get(Pose.ATTACK_LEFT_01).setFirstFrame(animDict.get(Pose.LEFT).getFirstFrame());
        animDict.get(Pose.ATTACK_RIGHT_01).setFirstFrame(animDict.get(Pose.RIGHT).getFirstFrame());
        animDict.get(Pose.ATTACK_DOWN_01).setFirstFrame(animDict.get(Pose.DOWN).getFirstFrame());
        animDict.get(Pose.ATTACK_UP_01).setFirstFrame(animDict.get(Pose.UP).getFirstFrame());
        animDict.get(Pose.ROLL_LEFT).setFirstFrame(animDict.get(Pose.LEFT).getFirstFrame());
        animDict.get(Pose.ROLL_RIGHT).setFirstFrame(animDict.get(Pose.RIGHT).getFirstFrame());
        animDict.get(Pose.ROLL_DOWN).setFirstFrame(animDict.get(Pose.DOWN).getFirstFrame());
        animDict.get(Pose.ROLL_UP).setFirstFrame(animDict.get(Pose.UP).getFirstFrame());
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

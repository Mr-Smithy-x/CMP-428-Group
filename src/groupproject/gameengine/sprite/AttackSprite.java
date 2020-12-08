package groupproject.gameengine.sprite;


import groupproject.containers.zelda.contracts.Damage;
import groupproject.containers.zelda.contracts.Energy;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.spritesheeteditor.models.PoseFileFormat;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public abstract class AttackSprite<P extends DamageProjectile> extends Sprite implements Energy, Damage {

    private double health;
    private double energy;

    public void isProjectileHitting(AttackSprite sprite) {
        if (getProjectile() != null) {
            getProjectile().damage(getProjectile(), sprite);
        }
    }

    protected AttackSprite(PoseFileFormat format, int x, int y, int scaled, int delay) throws IOException {
        super(format, x, y, scaled, delay);
    }

    public AttackSprite(String spriteSheet, int x, int y, int scaled, int delay) throws IOException {
        super(spriteSheet, x, y, scaled, delay);
    }

    public AttackSprite(int x, int y, String spritePrefix, int delay) {
        super(x, y, spritePrefix, delay);
    }

    public void attack(List<AttackSprite> enemies) {
        enemies.stream().filter(e -> e.distanceBetween(this) <= 50).forEach(e -> {
            if (getCurrentAnimation().isFirstFrame()) {
                switch (getDirection()) {
                    case NONE:
                        break;
                    case UP:
                        if (e.getY().intValue() < getY().intValue()) {
                            e.damageHealth(getDamagePoints() / 2);
                        }
                        break;
                    case DOWN:
                        if (e.getY().intValue() > getY().intValue()) {
                            e.damageHealth(getDamagePoints() / 2);
                        }
                        break;
                    case LEFT:
                        if (e.getX().intValue() < getX().intValue()) {
                            e.damageHealth(getDamagePoints() / 2);
                        }
                        break;
                    case RIGHT:
                        if (e.getX().intValue() > getX().intValue()) {
                            e.damageHealth(getDamagePoints() / 2);
                        }
                        break;
                }
            }
        });
        this.attack();
    }

    @Override
    public double getDamagePoints() {
        return getProjectile() != null ? getProjectile().getDamagePoints() : 0;
    }

    public abstract void setProjectile(P projectile);

    public abstract P getProjectile();

    protected void resetProjectile() {
        if (getProjectile() != null) {
            if (getProjectile().isOutsideCamera(GlobalCamera.getInstance())) {
                this.getProjectile().align(this);
                this.getProjectile().setVelocity(0);
            } else if (getProjectile().getVelocity() == 0) {
                this.getProjectile().align(this);
            }
        }
    }

    protected boolean shootWhen() {
        Pose[] poses = {Pose.ATTACK_LEFT_01, Pose.ATTACK_RIGHT_01, Pose.ATTACK_UP_01, Pose.ATTACK_DOWN_01};
        if (Arrays.stream(poses).anyMatch(p -> p == currentPose)) {
            return getCurrentAnimation().isLastFrame();
        }
        return false;
    }

    public boolean shoot() {
        if (getProjectile() != null) {
            canePose();
            if (shootWhen()) {
                getProjectile().setVelocity(20);
                getProjectile().playSound();
                useEnergy(.5);
                return true;
            } else {
                getCurrentAnimation().holdLastFrame();
            }
        }
        return false;
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

    public void spinAttack(List<AttackSprite> enemies) {
        enemies.stream().filter(e -> e.distanceBetween(this) <= 70).forEach(e -> {
            if (getCurrentAnimation().isFirstFrame()) {
                e.damageHealth(getDamagePoints() * 4);
            }
        });
        spin();
    }

    public void spin() {
        moving = true;
        setSpritePose(Pose.SPIN_ATTACK);
    }


    @Override
    protected void onInitAnimations() {
        animDict.values().forEach(a -> a.scale(scaled));
    }


    @Override
    public void render(Graphics g) {
        if (isDead()) {
            setSpritePose(Pose.DEAD);
        } else if (currentPose == Pose.DEAD) {
            setSpritePose(Pose.DOWN);
        }
        if (!isDead() && getProjectile() != null) {
            if (getProjectile().isInsideCamera(GlobalCamera.getInstance()) && getProjectile().getVelocity() > 0) {
                getProjectile().render(g);
                getProjectile().move();
            } else {
                resetProjectile();
            }
        }
        super.render(g);
    }


    @Override
    public void move() {
        super.move();
        if (getProjectile() != null && getProjectile().getVelocity() == 0) {
            getProjectile().align(this);
        }
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

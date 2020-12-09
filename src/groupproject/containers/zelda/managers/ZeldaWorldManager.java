package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.algorithms.ATileStarAlgorithm;
import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.Sprite;
import groupproject.gameengine.tile.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ZeldaWorldManager extends BaseWorldManager {

    ATileStarAlgorithm aStar;

    public ZeldaWorldManager(GameContainer container) {
        super(container);
    }

    /**
     * Handle the input events here
     *
     * @param keys
     */
    public void manual(boolean[] keys) {
        if (keys[KeyEvent.VK_D]) {
            getPlayer().damageHealth(1);
        } else if (keys[KeyEvent.VK_F]) {
            getPlayer().incrementHealth(1);
        }
        if (!isPlayerDead()) {
            if (keys[KeyEvent.VK_LEFT]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.LEFT);
                getPlayer().move();
            } else if (keys[KeyEvent.VK_RIGHT]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.RIGHT);
                getPlayer().move();
            } else if (keys[KeyEvent.VK_UP]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.UP);
                getPlayer().move();
            } else if (keys[KeyEvent.VK_DOWN]) {
                if (keys[KeyEvent.VK_R]) getPlayer().roll();
                else getPlayer().setSpritePose(Sprite.Pose.DOWN);
                getPlayer().move();
            }
            if (getPlayer().hasEnergy()) {
                if (keys[KeyEvent.VK_Z]) {
                    getPlayer().spinAttack(getEnemies());
                    getPlayer().useEnergy(.5);
                } else if (keys[KeyEvent.VK_SPACE]) {
                    getPlayer().attack(getEnemies());
                    getPlayer().useEnergy(.1);
                } else if (keys[KeyEvent.VK_T]) {
                    getPlayer().shoot();
                }
            }
        } else {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
        }
    }

    public void calculatePath(AttackSprite enemy) {
        aStar.solvePath(enemy, getPlayer());
    }

    /**
     * Enemy AI work and others
     */
    public void automate() {

        getEnemies().stream().filter(enemy -> !enemy.isDead()).forEach(enemy -> {
            getPlayer().isProjectileHitting(enemy);
            enemy.isProjectileHitting(getPlayer());
            if (enemy.isInsideCamera(GlobalCamera.getInstance())) {
                double distance = enemy.distanceBetween(getPlayer());
                if(enemy.isPathNullOrEmpty()) {
                    calculatePath(enemy);
                }
                if (distance <= 100) {
                    enemy.shoot();
                }
                enemy.automate();
            }
        });
    }

    @Override
    protected void renderGlobalSounds() {
        if (isPlayerDead() || getContainer().isPaused()) {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
        } else if (getPlayer().isNear(getEnemies())) {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.COMBAT);
        } else {
            GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.NORMAL);
        }
        GlobalSoundEffect.getInstance().play(getPlayer());
        GlobalSoundTrack.getInstance().play();
    }

    @Override
    protected void renderGame(Graphics g) {
        super.renderGame(g);
        //The game already handles most of the rendering, if there any more rendering that needs to be done
        //do it here
    }

    @Override
    public void setTileMap(TileMap map) {
        super.setTileMap(map);
        aStar = new ATileStarAlgorithm(map);
    }


    public ATileStarAlgorithm getAStar() {
        return aStar;
    }


}

package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.camera.GlobalCamera;
import groupproject.gameengine.sprite.AttackSprite;
import groupproject.gameengine.sprite.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ZeldaWorldManager extends BaseWorldManager {

    public ZeldaWorldManager(GameContainer container) {
        super(container);
    }

    /**
     * Handle the input events here
     *
     * @param keys
     */
    public void manual(boolean[] keys) {

        if (keys[KeyEvent.VK_9]) getContainer().setDebug(true);
        else if (keys[KeyEvent.VK_0]) getContainer().setDebug(false);

        // Used while debugging.
        if (getContainer().inDebuggingMode()) {
            if (keys[KeyEvent.VK_D]) getPlayer().damageHealth(1);
            else if (keys[KeyEvent.VK_F]) getPlayer().incrementHealth(1);
        }

        if (!isPlayerDead()) {
            handlePlayerMovement(keys);

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

        } else GlobalSoundTrack.getInstance().setTrack(GlobalSoundTrack.Track.PAUSE);
    }

    private void handlePlayerMovement(boolean[] keys) {
        if ((keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_W]) && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.LEFT)) {
            getPlayer().setSpritePose(Sprite.Pose.LEFT);
            getPlayer().move();
        } else if (keys[KeyEvent.VK_RIGHT] && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.RIGHT)) {
            getPlayer().setSpritePose(Sprite.Pose.RIGHT);
            getPlayer().move();
        } else if (keys[KeyEvent.VK_UP] && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.UP)) {
            getPlayer().setSpritePose(Sprite.Pose.UP);
            getPlayer().move();
        } else if (keys[KeyEvent.VK_DOWN] && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.DOWN)) {
            getPlayer().setSpritePose(Sprite.Pose.DOWN);
            getPlayer().move();
        }
    }

    public void calculatePath(AttackSprite enemy) {
        mapManager.getAStar().solvePath(enemy, getPlayer());
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
}

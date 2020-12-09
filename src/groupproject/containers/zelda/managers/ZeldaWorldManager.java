package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.sound.GlobalSoundEffect;
import groupproject.containers.zelda.sound.GlobalSoundTrack;
import groupproject.gameengine.GameContainer;
import groupproject.gameengine.sprite.Sprite;
import groupproject.games.ZeldaTestGame;

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

        if (keys[KeyEvent.VK_9]) {
            System.setProperty("DEBUG", "true");
        } else if (keys[KeyEvent.VK_0]) {
            System.setProperty("DEBUG", "false");
        }

        // Used while debugging.
        if (ZeldaTestGame.inDebuggingMode()) {
            if (keys[KeyEvent.VK_D]) getPlayer().damageHealth(1);
            else if (keys[KeyEvent.VK_F]) getPlayer().incrementHealth(1);
        }

        if (!isPlayerDead()) {
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

    /**
     * Enemy AI work and others
     */
    public void automate() {
        getEnemies().stream().filter(enemy -> !enemy.isDead()).forEach(enemy -> {
            getPlayer().isProjectileHitting(enemy);
            enemy.isProjectileHitting(getPlayer());
            if (enemy.distanceBetween(getPlayer()) > 100) {
                if (enemy.getX().intValue() > getPlayer().getX().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.LEFT);
                    enemy.move();
                }
                if (enemy.getY().intValue() > getPlayer().getY().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.UP);
                    enemy.move();
                }
                if (enemy.getX().intValue() < getPlayer().getX().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.RIGHT);
                    enemy.move();
                }
                if (enemy.getY().intValue() < getPlayer().getY().intValue()) {
                    enemy.setSpritePose(Sprite.Pose.DOWN);
                    enemy.move();
                }
            } else {
                //Basic AI, we can improve this
                enemy.shoot();
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

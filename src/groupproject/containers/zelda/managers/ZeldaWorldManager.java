package groupproject.containers.zelda.managers;

import groupproject.containers.zelda.models.TransitionTile;
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
            if (keys[KeyEvent.VK_G]) getPlayer().damageHealth(1);
            else if (keys[KeyEvent.VK_H]) getPlayer().incrementHealth(1);
            else if (keys[KeyEvent.VK_8]) {
                MapManager.getInstance().loadTileMap("hyrule_castle_entrance");
            }
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
        if ((keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.LEFT)) {
            getPlayer().setSpritePose(Sprite.Pose.LEFT);
            getPlayer().move();
        } else if ((keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.RIGHT)) {
            getPlayer().setSpritePose(Sprite.Pose.RIGHT);
            getPlayer().move();
        } else if ((keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.UP)) {
            getPlayer().setSpritePose(Sprite.Pose.UP);
            getPlayer().move();
        } else if ((keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) && !mapManager.isSpriteCollidingWithMap(getPlayer(), Sprite.Pose.DOWN)) {
            getPlayer().setSpritePose(Sprite.Pose.DOWN);
            getPlayer().move();
        }
        TransitionTile transitionTile = MapManager.getInstance().checkTransitionTile(getPlayer());
        if (transitionTile.getMap() != TransitionTile.Map.NONE) {
            if (inDebuggingMode()) {
                System.out.printf("Entering: %s\n", transitionTile.getMap());
            }
            MapManager.getInstance().transition(getPlayer(), transitionTile);
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
                if (enemy.isPathNullOrEmpty()) {
                    calculatePath(enemy);
                }
                if (distance <= 100) {
                    if(enemy.isAbove(getPlayer())){
                        enemy.setSpritePose(Sprite.Pose.DOWN);
                    }else if(enemy.isBelow(getPlayer())){
                        enemy.setSpritePose(Sprite.Pose.UP);
                    }
                    if(enemy.isRightOf(getPlayer())){
                        enemy.setSpritePose(Sprite.Pose.LEFT);
                    }else if(enemy.isLeftOf(getPlayer())){
                        enemy.setSpritePose(Sprite.Pose.RIGHT);
                    }

                    enemy.shoot();
                    enemy.isProjectileHitting(getPlayer());
                    if(enemy.isOverlapping(getPlayer()) &&
                            (enemy.getX().intValue() == getPlayer().getX().intValue()) &&
                                (enemy.getY().intValue() == getPlayer().getY().intValue())){
                        getPlayer().damageHealth(0.5);
                    }
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
